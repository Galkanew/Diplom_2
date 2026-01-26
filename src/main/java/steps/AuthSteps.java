package steps;

import constants.StatusCode;
import constants.TextResponse;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.User;
import static org.hamcrest.Matchers.equalTo;

public class AuthSteps {

    @Step("Авторизация пользователя")
    public static Response loginUser(User user) {
        return clients.AuthClient.login(user);
    }

    @Step("Проверка успешной авторизации")
    public static void checkLoginSuccessful(Response response, User user) {
        response.then()
                .statusCode(StatusCode.OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(user.getEmail()))
                .body("user.name", equalTo(user.getName()));
    }

    @Step("Проверка ошибки 'Неверные учетные данные'")
    public static void checkInvalidCredentialsError(Response response) {
        response.then()
                .statusCode(StatusCode.UNAUTHORIZED)
                .body("success", equalTo(false))
                .body("message", equalTo(TextResponse.INVALID_CREDENTIALS));
    }
}