package ru.yandex.scooter.client;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static ru.yandex.scooter.Constants.SCOOTER_BASE_URI;

public abstract class RestAssuredClient {

    RequestSpecification createRequestSpecification() {
        return given().baseUri(SCOOTER_BASE_URI).log().body();
    }
}
