package ru.yandex.scooter.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.yandex.scooter.model.courier.CourierCredentials;
import ru.yandex.scooter.model.courier.CourierData;

import static io.restassured.http.ContentType.JSON;
import static ru.yandex.scooter.Constants.Courier.*;

public class CourierClient extends RestAssuredClient {
    @Step
    public ValidatableResponse createCourier(CourierData courierData) {
        return createRequestSpecification()
                .contentType(JSON)
                .body(courierData)
                .when()
                .post(CREATE_COURIER_PATH)
                .then().log().all();
    }

    @Step
    public ValidatableResponse loginCourier(CourierCredentials credentials) {
        return createRequestSpecification()
                .contentType(JSON)
                .body(credentials)
                .when()
                .post(LOGIN_COURIER_PATH)
                .then().log().all();
    }

    @Step
    public ValidatableResponse deleteCourier(Integer id) {
        return createRequestSpecification()
                .pathParam(ID_PARAM, id)
                .when()
                .delete(DELETE_COURIER_PATH)
                .then().log().all();
    }
}
