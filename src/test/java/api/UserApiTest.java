package api;

import helpers.DataGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.User;
import org.junit.Test;
import steps.UserSteps;

import static steps.UserSteps.checkUserAlreadyExistsError;
import static steps.UserSteps.checkUserCreatedSuccessfully;
import static steps.UserSteps.createUser;

public class UserApiTest extends BaseTest {

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUserSuccess() {
        User uniqueUser = DataGenerator.getRandomUser();
        Response response = createUser(uniqueUser);
        checkUserCreatedSuccessfully(response, uniqueUser);
    }
}