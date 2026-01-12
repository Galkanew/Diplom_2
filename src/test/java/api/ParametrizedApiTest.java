package api;

import helpers.DataGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.User;
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
                }
        });
    }

    @Test
    @DisplayName("Параметризированный тест создания пользователя")
    public void parametrizedCreateUserTest() {
        Response response = clients.UserClient.createUser(user);

        response.then()
                .statusCode(expectedStatusCode)
                .body("success", equalTo(expectedSuccess));

        if (expectedMessage != null) {
            response.then()
                    .body("message", equalTo(expectedMessage));
        }
    }
}