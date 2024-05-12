package ru.yandex.scooter.courier;

import org.junit.After;
import org.junit.Test;
import ru.yandex.scooter.client.CourierClient;
import ru.yandex.scooter.model.courier.CourierCredentials;
import ru.yandex.scooter.model.courier.CourierData;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static ru.yandex.scooter.Constants.Courier.NOT_ENOUGH_LOGIN_DATA_MESSAGE;
import static ru.yandex.scooter.Constants.Courier.USER_NOT_FOUND_MESSAGE;
import static ru.yandex.scooter.model.courier.CourierData.generateRandomLogin;

public class LoginCourierTest {
    public final CourierClient courierClient = new CourierClient();
    private Integer courierId;

    @Test
    public void loggedInSuccessfully() {
        CourierData courierData = new CourierData(generateRandomLogin(), "testPassword", "testName");
        courierClient.createCourier(courierData);

        CourierCredentials credentials = CourierCredentials.from(courierData);
        courierId = courierClient.loginCourier(credentials)
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path("id");

        assertNotNull(courierId);
    }

    @After
    public void deleteCourier() {
        if (courierId != null) {
            courierClient.deleteCourier(courierId)
                    .assertThat()
                    .body("ok", equalTo(true));
        }
    }

    @Test
    public void loginWithoutUserName() {
        CourierCredentials credentials = new CourierCredentials(null, "testPassword");
        courierClient.loginCourier(credentials)
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .body("message", equalTo(NOT_ENOUGH_LOGIN_DATA_MESSAGE));
    }

    @Test
    public void loginWithoutPassword() {
        CourierCredentials credentials = new CourierCredentials("testLogin", null);
        courierClient.loginCourier(credentials)
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .body("message", equalTo(NOT_ENOUGH_LOGIN_DATA_MESSAGE));
    }

    @Test
    public void loginWithIncorrectUserName() {
        CourierData courierData = new CourierData(generateRandomLogin(), "testPassword", "testName");
        courierClient.createCourier(courierData); //todo надо ли проверять успешность созданного курьера - вынести чеки? переименовать логины в юзернэймы

        CourierCredentials credentials = new CourierCredentials(courierData.getLogin() + "_incorrect", courierData.getPassword());
        courierClient.loginCourier(credentials)
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .body("message", equalTo(USER_NOT_FOUND_MESSAGE));
    }

    @Test
    public void loginWithIncorrectPassword() {
        CourierData courierData = new CourierData(generateRandomLogin(), "testPassword", "testName");
        courierClient.createCourier(courierData);

        CourierCredentials credentials = new CourierCredentials(courierData.getLogin(), "incorrectPassword");
        courierClient.loginCourier(credentials)
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .body("message", equalTo(USER_NOT_FOUND_MESSAGE));
    }

    @Test
    public void nonExistingUserLogin() {
        CourierCredentials credentials = new CourierCredentials("nonExistingUserName", "testPassword");
        courierClient.loginCourier(credentials)
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .body("message", equalTo(USER_NOT_FOUND_MESSAGE));
    }
}