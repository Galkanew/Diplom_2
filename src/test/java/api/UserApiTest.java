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

    @Test
    @DisplayName("Попытка создания пользователя с уже существующим email должна завершиться ошибкой")
    public void createUserWithExistingEmailFails() {
        // Создаём первоначального пользователя
        User firstUser = DataGenerator.getRandomUser();
        Response firstResponse = createUser(firstUser);

        // Проверяем, что первый пользователь успешно создан
        checkUserCreatedSuccessfully(firstResponse, firstUser);

        // Пытаемся создать второго пользователя с тем же email
        User secondUser = User.builder()
                .email(firstUser.getEmail())  // Тот же email
                .password("another_password_123")
                .name("Another Name")
                .build();

        Response secondResponse = createUser(secondUser);

        // Проверяем, что API возвращает ошибку о существовании пользователя
        checkUserAlreadyExistsError(secondResponse);
    }
}
