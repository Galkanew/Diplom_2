package steps;

import constants.StatusCode;
import constants.TextResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.Order;
import static org.hamcrest.Matchers.equalTo;

public class OrderSteps {

    @Step("Создание заказа с авторизацией")
    public static Response createOrderWithAuth(Order order, String accessToken) {
        return clients.OrderClient.createOrder(order, accessToken);
    }

    @Step("Создание заказа без авторизации")
    public static Response createOrderWithoutAuth(Order order) {
        return clients.OrderClient.createOrderWithoutAuth(order);
    }

    @Step("Проверка успешного создания заказа")
    public static void checkOrderCreatedSuccessfully(Response response) {
        response.then()
                .statusCode(StatusCode.OK)
                .body("success", equalTo(true));
    }

    @Step("Проверка ошибки 'Требуется авторизация'")
    public static void checkAuthorizationRequiredError(Response response) {
        response.then()
                .statusCode(StatusCode.UNAUTHORIZED)
                .body("message", equalTo(TextResponse.AUTHORIZATION_REQUIRED));
    }

    @Step("Проверка ошибки 'Не указаны ингредиенты'")
    public static void checkIngredientsRequiredError(Response response) {
        response.then()
                .statusCode(StatusCode.BAD_REQUEST)
                .body("success", equalTo(false))
                .body("message", equalTo(TextResponse.INGREDIENTS_REQUIRED));
    }
}