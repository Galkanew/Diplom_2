package steps;

import constants.StatusCode;
import constants.TextResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.User;
import static org.hamcrest.Matchers.equalTo;

public class UserSteps {

    @Step("Создание пользователя")
    public static Response createUser(User user) {
        return clients.UserClient.createUser(user);
    }

    @Step("Проверка успешного создания пользователя")
    public static void checkUserCreatedSuccessfully(Response response, User user) {
        response.then()
                .statusCode(StatusCode.OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()));
    }

    @Step("Проверка ошибки 'Пользователь уже существует'")
    public static void checkUserAlreadyExistsError(Response response) {
        response.then()
                .statusCode(StatusCode.FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo(TextResponse.USER_ALREADY_EXISTS));
    }

    @Step("Проверка ошибки 'Обязательные поля'")
    public static void checkRequiredFieldsError(Response response) {
        response.then()
                .statusCode(StatusCode.FORBIDDEN)
                .body("success", equalTo(false))
                .body("message", equalTo(TextResponse.REQUIRED_FIELDS));
    }
}