package ru.yandex.scooter.order;

import io.qameta.allure.Description;
import org.junit.Test;
import ru.yandex.scooter.client.OrderClient;
import ru.yandex.scooter.model.order.GetOrdersData;
import ru.yandex.scooter.model.order.OrderData;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.junit.Assert.assertNotEquals;
import static ru.yandex.scooter.Constants.Order.ORDERS_KEY;

public class GetOrdersTest {
    private final OrderClient orderClient = new OrderClient();

    @Test
    @Description("Get orders by limit and page and check that order list is not empty")
    public void getOrders() {
        String limit = "5";
        String page = "0";
        GetOrdersData ordersData = new GetOrdersData(null, null, limit, page);

        OrderData[] orders = orderClient
                .getOrders(ordersData)
                .assertThat()
                .statusCode(HTTP_OK)
                .extract().response().body().jsonPath().getObject(ORDERS_KEY, OrderData[].class);

        assertNotEquals(0, orders.length);
    }
}