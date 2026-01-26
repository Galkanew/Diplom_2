package api;

import helpers.DataGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static constants.StatusCode.*;
import static constants.TextResponse.*;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
@DisplayName("Параметризированные тесты создания пользователя")
public class ParametrizedApiTest extends BaseTest {

    private static User existingUser;

    @Before
    public void setUp() {
        // Создаем пользователя только один раз для всех тестов
        if (existingUser == null) {
            existingUser = DataGenerator.getRandomUser();
            clients.UserClient.createUser(existingUser);
        }
    }

    private final User user;
    private final int expectedStatusCode;
    private final String expectedMessage;
    private final boolean expectedSuccess;
    private final String testDescription;

    public ParametrizedApiTest(User user, int expectedStatusCode,
                               String expectedMessage, boolean expectedSuccess,
                               String testDescription) {
        this.user = user;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedMessage = expectedMessage;
        this.expectedSuccess = expectedSuccess;
        this.testDescription = testDescription;
    }

    @Parameterized.Parameters(name = "{4}")
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][] {
                {
                        DataGenerator.getRandomUser(),
                        OK,
                        null,
                        true,
                        "Создание пользователя с валидными данными"
                },
                {
                        DataGenerator.getUserWithoutName(),
                        FORBIDDEN,
                        REQUIRED_FIELDS,
                        false,
                        "Создание пользователя без имени"
                },
                {
                        DataGenerator.getUserWithoutEmail(),
                        FORBIDDEN,
                        REQUIRED_FIELDS,
                        false,
                        "Создание пользователя без email"
                },
                {
                        DataGenerator.getUserWithoutPassword(),
                        FORBIDDEN,
                        REQUIRED_FIELDS,
                        false,
                        "Создание пользователя без пароля"
                },
                {

                       User.builder()
                                .email(existingUser != null ? existingUser.getEmail() : "existing@example.com")
                                .password("any_password")
                                .name("Any Name")
                                .build(),
                        FORBIDDEN,
                        USER_ALREADY_EXISTS,
                        false,
                        "Создание пользователя с уже существующим email"
                }
        });
    }

    @Test
    @DisplayName("Параметризированный тест создания пользователя")
    public void parametrizedCreateUserTest() {
        // Для теста с дубликатом email используем email существующего пользователя
        User testUser = this.user;
        if (testDescription.equals("Создание пользователя с уже существующим email") && existingUser != null) {
            testUser = testUser.toBuilder()
                    .email(existingUser.getEmail())
                    .build();
        }

        Response response = clients.UserClient.createUser(testUser);

        response.then()
                .statusCode(expectedStatusCode)
                .body("success", equalTo(expectedSuccess));

        if (expectedMessage != null) {
            response.then()
                    .body("message", equalTo(expectedMessage));
        }
    }
}