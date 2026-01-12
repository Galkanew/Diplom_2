package api;

import helpers.DataGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.User;
import org.junit.Before;
import org.junit.Test;
import steps.AuthSteps;
import steps.UserSteps;

import static steps.AuthSteps.checkInvalidCredentialsError;
import static steps.AuthSteps.checkLoginSuccessful;
import static steps.AuthSteps.loginUser;

public class AuthApiTest extends BaseTest {

    private User registeredUser;

    @Before
    public void setUp() {
        registeredUser = DataGenerator.getRandomUser();
        UserSteps.createUser(registeredUser);
    }

    @Test
    @DisplayName("Вход под существующим пользователем")
    public void loginExistingUserSuccess() {
        Response response = loginUser(registeredUser);
        checkLoginSuccessful(response, registeredUser);
    }

    @Test
    @DisplayName("Вход с неверным паролем")
    public void loginWithWrongPasswordError() {
        User wrongPasswordUser = registeredUser.toBuilder()
                .password("wrong_password")
                .build();

        Response response = clients.AuthClient.login(wrongPasswordUser);
        checkInvalidCredentialsError(response);
    }
}