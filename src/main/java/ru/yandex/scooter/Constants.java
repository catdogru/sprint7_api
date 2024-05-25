package ru.yandex.scooter;

public interface Constants {
    String SCOOTER_BASE_URI = "https://qa-scooter.praktikum-services.ru";
    String API_V1_PATH = "/api/v1/";

    interface Courier {
        // api path
        String CREATE_COURIER_PATH = API_V1_PATH + "courier";
        String LOGIN_COURIER_PATH = API_V1_PATH + "courier/login";
        String DELETE_COURIER_PATH = API_V1_PATH + "courier/{id}";

        // response message
        String NOT_ENOUGH_CREATION_DATA_MESSAGE = "Недостаточно данных для создания учетной записи";
        String LOGIN_ALREADY_USED_MESSAGE = "Этот логин уже используется";
        String NOT_ENOUGH_LOGIN_DATA_MESSAGE = "Недостаточно данных для входа";
        String USER_NOT_FOUND_MESSAGE = "Учетная запись не найдена";

        // json keys
        String OK_KEY = "ok";
        String MESSAGE_KEY = "message";
        String ID_KEY = "id";

        // params
        String ID_PARAM = "id";
    }

    interface Order {
        //api path
        String CREATE_ORDER_PATH = API_V1_PATH + "orders";
        String GET_ORDERS_PATH = API_V1_PATH + "orders";
        String CANCEL_ORDER_PATH = API_V1_PATH + "orders/cancel";

        // json keys
        String TRACK_KEY = "track";
        String OK_KEY = "ok";
        String ORDERS_KEY = "orders";

        // params
        String TRACK_PARAM = "track";
        String COURIER_ID_PARAM = "courierId";
        String LIMIT_PARAM = "limit";
        String PAGE_PARAM = "page";
        String NEAREST_STATION_PARAM = "nearestStation";

        // enums
        enum ScooterColor {
            BLACK,
            GREY
        }
    }
}
