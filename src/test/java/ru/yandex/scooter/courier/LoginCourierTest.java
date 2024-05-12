package ru.yandex.scooter.courier;

import io.qameta.allure.Description;
import org.junit.After;
import org.junit.Test;
import ru.yandex.scooter.client.CourierClient;
import ru.yandex.scooter.model.courier.CourierCredentials;
import ru.yandex.scooter.model.courier.CourierData;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static ru.yandex.scooter.Constants.Courier.*;
import static ru.yandex.scooter.model.courier.CourierData.generateRandomLogin;

public class LoginCourierTest {
    public final CourierClient courierClient = new CourierClient();
    private Integer courierId;

    @Test
    @Description("Create and log in courier, check that courier id is not empty")
    public void loggedInSuccessfully() {
        CourierData courierData = new CourierData(generateRandomLogin(), "testPassword", "testName");
        courierClient.createCourier(courierData);

        CourierCredentials credentials = CourierCredentials.from(courierData);
        courierId = courierClient.loginCourier(credentials)
                .assertThat()
                .statusCode(HTTP_OK)
                .extract()
                .path(ID_KEY);

        assertNotNull(courierId);
    }

    @After
    public void deleteCourier() {
        if (courierId != null) {
            courierClient.deleteCourier(courierId)
                    .assertThat()
                    .body(OK_KEY, equalTo(true));
        }
    }

    @Test
    @Description("Try to login without login field and check error message")
    public void loginWithoutUserName() {
        CourierCredentials credentials = new CourierCredentials(null, "testPassword");
        courierClient.loginCourier(credentials)
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .body(MESSAGE_KEY, equalTo(NOT_ENOUGH_LOGIN_DATA_MESSAGE));
    }

    @Test
    @Description("Try to login without password field and check error message")
    public void loginWithoutPassword() {
        CourierCredentials credentials = new CourierCredentials("testLogin", null);
        courierClient.loginCourier(credentials)
                .assertThat()
                .statusCode(HTTP_BAD_REQUEST)
                .and()
                .body(MESSAGE_KEY, equalTo(NOT_ENOUGH_LOGIN_DATA_MESSAGE));
    }

    @Test
    @Description("Try to login with invalid username and check error message")
    public void loginWithIncorrectUserName() {
        CourierData courierData = new CourierData(generateRandomLogin(), "testPassword", "testName");
        courierClient.createCourier(courierData);

        CourierCredentials credentials = new CourierCredentials(courierData.getLogin() + "_incorrect", courierData.getPassword());
        courierClient.loginCourier(credentials)
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .body(MESSAGE_KEY, equalTo(USER_NOT_FOUND_MESSAGE));
    }

    @Test
    @Description("Try to login with invalid password and check error message")
    public void loginWithIncorrectPassword() {
        CourierData courierData = new CourierData(generateRandomLogin(), "testPassword", "testName");
        courierClient.createCourier(courierData);

        CourierCredentials credentials = new CourierCredentials(courierData.getLogin(), "incorrectPassword");
        courierClient.loginCourier(credentials)
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .body(MESSAGE_KEY, equalTo(USER_NOT_FOUND_MESSAGE));
    }

    @Test
    @Description("Try to login with non-existent username and check error message")
    public void nonExistingUserLogin() {
        CourierCredentials credentials = new CourierCredentials("nonExistingUserName", "testPassword");
        courierClient.loginCourier(credentials)
                .assertThat()
                .statusCode(HTTP_NOT_FOUND)
                .and()
                .body(MESSAGE_KEY, equalTo(USER_NOT_FOUND_MESSAGE));
    }
}