package ru.yandex.scooter.client;

import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import ru.yandex.scooter.model.order.GetOrdersData;
import ru.yandex.scooter.model.order.OrderData;

import static ru.yandex.scooter.Constants.Order.*;

public class OrderClient extends RestAssuredClient {

    public ValidatableResponse createOrder(OrderData orderData) {
        return createRequestSpecification()
                .body(orderData)
                .when()
                .post(CREATE_ORDER_PATH)
                .then()
                .log().all();
    }

    public ValidatableResponse cancelOrder(Integer track) {
        return createRequestSpecification()
                .queryParam("track", track)
                .when()
                .put(CANCEL_ORDER_PATH)
                .then()
                .log().all();
    }

    public ValidatableResponse getOrders(GetOrdersData ordersData) {
        RequestSpecification specification = createRequestSpecification();

        if (ordersData.getCourierId() != null) specification.queryParam("courierId", ordersData.getCourierId());
        if (ordersData.getLimit() != null) specification.queryParam("limit", ordersData.getLimit());
        if (ordersData.getPage() != null) specification.queryParam("page", ordersData.getPage());
        if (ordersData.getNearestStation() != null)
            specification.queryParam("nearestStation", ordersData.getNearestStation());

        return specification
                .get(GET_ORDERS_PATH)
                .then()
                .log().all();
    }
}
