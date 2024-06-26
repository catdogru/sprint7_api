package ru.yandex.scooter.courier;

import io.qameta.allure.Description;
import io.restassured.response.ValidatableResponse;
import org.junit.Test;
import ru.yandex.scooter.client.CourierClient;
import ru.yandex.scooter.model.courier.CourierData;

import static java.net.HttpURLConnection.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertEquals;
import static ru.yandex.scooter.Constants.Courier.*;
import static ru.yandex.scooter.model.courier.CourierData.generateRandomLogin;

public class CreateCourierTest {
    public final CourierClient courierClient = new CourierClient();

    @Test
    @Description("Create courier and check success message")
    public void createdSuccessfully() {
        CourierData courierData = new CourierData(generateRandomLogin(), "testPass", "testName");
        courierClient.createCourier(courierData).assertThat()
                .statusCode(HTTP_CREATED)
                .and()
                .body(OK_KEY, equalTo(true));
    }

    @Test
    @Description("Try to create courier without login field and check error message")
    public void createWithoutLogin() {
        CourierData courierData = new CourierData(null, "testPass", "testName");
        ValidatableResponse response = courierClient.createCourier(courierData);
        String messageText = response.assertThat().statusCode(HTTP_BAD_REQUEST).extract().path(MESSAGE_KEY);
        assertEquals(NOT_ENOUGH_CREATION_DATA_MESSAGE, messageText);
    }

    @Test
    @Description("Try to create courier without password field and check error message")
    public void createWithoutPassword() {
        CourierData courierData = new CourierData(generateRandomLogin(), null, "testName");
        ValidatableResponse response = courierClient.createCourier(courierData);
        String messageText = response.assertThat().statusCode(HTTP_BAD_REQUEST).extract().path(MESSAGE_KEY);
        assertEquals(NOT_ENOUGH_CREATION_DATA_MESSAGE, messageText);
    }

    @Test
    @Description("Try to create identical couriers and check error message")
    public void createSameCouriers() {
        CourierData sameCourierData = new CourierData(generateRandomLogin(), "testPass", "testName");
        // first creation
        courierClient.createCourier(sameCourierData);
        // second creation
        courierClient.createCourier(sameCourierData)
                .assertThat()
                .statusCode(HTTP_CONFLICT)
                .and().body(MESSAGE_KEY, equalTo(LOGIN_ALREADY_USED_MESSAGE));
    }

    @Test
    @Description("Try to create courier with existing login and check error message")
    public void createWithExistingLogin() {
        String sameLogin = generateRandomLogin();

        CourierData firstCourierData = new CourierData(sameLogin, "firstTestPass", "firstTestName");
        CourierData secondCourierData = new CourierData(sameLogin, "secondTestPass", "secondTestName");

        courierClient.createCourier(firstCourierData);
        courierClient.createCourier(secondCourierData)
                .assertThat()
                .statusCode(HTTP_CONFLICT)
                .and().body(MESSAGE_KEY, equalTo(LOGIN_ALREADY_USED_MESSAGE));
    }
}