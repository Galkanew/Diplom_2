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
@DisplayName("Параметризированные тесты авторизации пользователя")
public class ParametrizedAuthTest extends BaseTest {

    private User registeredUser;

    @Before
    public void setUp() {
        registeredUser = DataGenerator.getRandomUser();
        clients.UserClient.createUser(registeredUser);
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][] {
                {
                        "Вход с верными учетными данными",
                        User.builder()
                                .email("correct_email_will_be_replaced")
                                .password("correct_password_will_be_replaced")
                                .build(),
                        OK,
                        null,
                        true
                },
                {
                        "Вход с неверным паролем",
                        User.builder()
                                .email("correct_email_will_be_replaced")
                                .password("wrong_password")
                                .build(),
                        UNAUTHORIZED,
                        INVALID_CREDENTIALS,
                        false
                },
                {
                        "Вход с несуществующим email",
                        User.builder()
                                .email("nonexistent@example.com")
                                .password("any_password")
                                .build(),
                        UNAUTHORIZED,
                        INVALID_CREDENTIALS,
                        false
                },
                {
                        "Вход с пустым паролем",
                        User.builder()
                                .email("correct_email_will_be_replaced")
                                .password("")
                                .build(),
                        UNAUTHORIZED,
                        INVALID_CREDENTIALS,
                        false
                }
        });
    }

    private final String testName;
    private final User loginUser;
    private final int expectedStatusCode;
    private final String expectedMessage;
    private final boolean expectedSuccess;

    public ParametrizedAuthTest(String testName, User loginUser,
                                int expectedStatusCode, String expectedMessage,
                                boolean expectedSuccess) {
        this.testName = testName;
        this.loginUser = loginUser;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedMessage = expectedMessage;
        this.expectedSuccess = expectedSuccess;
    }

    @Test
    @DisplayName("Параметризированный тест авторизации")
    public void parametrizedLoginTest() {
        User actualLoginUser = loginUser;

        if (loginUser.getEmail().equals("correct_email_will_be_replaced")) {
            actualLoginUser = loginUser.toBuilder()
                    .email(registeredUser.getEmail())
                    .build();
        }

        if (loginUser.getPassword().equals("correct_password_will_be_replaced")) {
            actualLoginUser = actualLoginUser.toBuilder()
                    .password(registeredUser.getPassword())
                    .build();
        }

        Response response = clients.AuthClient.login(actualLoginUser);

        response.then()
                .statusCode(expectedStatusCode)
                .body("success", equalTo(expectedSuccess));

        if (expectedMessage != null) {
            response.then()
                    .body("message", equalTo(expectedMessage));
        }
    }
}