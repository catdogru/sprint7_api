package ru.yandex.scooter.order;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.scooter.Constants.Order.ScooterColor;
import ru.yandex.scooter.client.OrderClient;
import ru.yandex.scooter.model.order.OrderData;

import static java.net.HttpURLConnection.HTTP_CREATED;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static ru.yandex.scooter.Constants.Order.OK_KEY;
import static ru.yandex.scooter.Constants.Order.ScooterColor.BLACK;
import static ru.yandex.scooter.Constants.Order.ScooterColor.GREY;
import static ru.yandex.scooter.Constants.Order.TRACK_KEY;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final OrderClient orderClient = new OrderClient();
    private final OrderData orderData;
    private Integer orderTrack;

    public CreateOrderTest(ScooterColor[] scooterColors) {
        orderData = new OrderData(
                "Тест",
                "Тестов",
                "ул. Тестовая, д. Тестовый, кв. 0",
                "Тестовская",
                "+7-000-123-45-67",
                3,
                "1970-01-01",
                "Тестовый комментарий",
                scooterColors
        );
    }

    @Parameterized.Parameters()
    public static Object[][] getData() {
        return new Object[][]{
                {new ScooterColor[]{BLACK, GREY}},
                {new ScooterColor[]{GREY}},
                {new ScooterColor[]{BLACK}},
                {null}
        };
    }

    @Test
    public void createOrder() {
        orderTrack = orderClient.createOrder(orderData)
                .assertThat()
                .statusCode(HTTP_CREATED)
                .extract()
                .path(TRACK_KEY);

        assertNotNull(orderTrack);
    }

    @After
    public void cancelOrder() {
        if (orderTrack != null) {
            orderClient.cancelOrder(orderTrack)
                    .assertThat()
                    .body(OK_KEY, equalTo(true));
        }
    }
}