package ru.yandex.scooter.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import ru.yandex.scooter.model.order.GetOrdersData;
import ru.yandex.scooter.model.order.OrderData;

import static ru.yandex.scooter.Constants.Order.*;

public class OrderClient extends RestAssuredClient {
    @Step
    public ValidatableResponse createOrder(OrderData orderData) {
        return createRequestSpecification()
                .body(orderData)
                .when()
                .post(CREATE_ORDER_PATH)
                .then()
                .log().all();
    }

    @Step
    public ValidatableResponse cancelOrder(Integer track) {
        return createRequestSpecification()
                .queryParam(TRACK_PARAM, track)
                .when()
                .put(CANCEL_ORDER_PATH)
                .then()
                .log().all();
    }

    @Step
    public ValidatableResponse getOrders(GetOrdersData ordersData) {
        RequestSpecification specification = createRequestSpecification();

        if (ordersData.getCourierId() != null) specification.queryParam(COURIER_ID_PARAM, ordersData.getCourierId());
        if (ordersData.getLimit() != null) specification.queryParam(LIMIT_PARAM, ordersData.getLimit());
        if (ordersData.getPage() != null) specification.queryParam(PAGE_PARAM, ordersData.getPage());
        if (ordersData.getNearestStation() != null)
            specification.queryParam(NEAREST_STATION_PARAM, ordersData.getNearestStation());

        return specification
                .get(GET_ORDERS_PATH)
                .then()
                .log().all();
    }
}
