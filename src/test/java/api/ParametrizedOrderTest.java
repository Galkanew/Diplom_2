package api;

import helpers.DataGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Order;
import models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static constants.StatusCode.*;
import static constants.TextResponse.*;
import static org.hamcrest.Matchers.equalTo;

@RunWith(Parameterized.class)
@DisplayName("Параметризированные тесты создания заказа")
public class ParametrizedOrderTest extends BaseTest {

    private String accessToken;
    private String validIngredientId;

    @Before
    public void setUp() {
        User registeredUser = DataGenerator.getRandomUser();
        clients.UserClient.createUser(registeredUser);
        accessToken = clients.AuthClient.getAccessToken(registeredUser);
        validIngredientId = clients.OrderClient.getFirstIngredientId();
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][] {
                {
                        "Заказ с авторизацией и валидным ингредиентом",
                        true,
                        Collections.singletonList("valid_ingredient_will_be_replaced"),
                        OK,
                        null,
                        true
                },
                {
                        "Заказ без авторизации",
                        false,
                        Collections.singletonList("valid_ingredient_will_be_replaced"),
                        UNAUTHORIZED,
                        AUTHORIZATION_REQUIRED,
                        false
                },
                {
                        "Заказ без ингредиентов",
                        true,
                        Collections.emptyList(),
                        BAD_REQUEST,
                        INGREDIENTS_REQUIRED,
                        false
                },
                {
                        "Заказ с неверным хешем ингредиента",
                        true,
                        Collections.singletonList("invalid_hash_123"),
                        INTERNAL_SERVER_ERROR,
                        null,
                        false
                }
        });
    }

    private final String testName;
    private final boolean useAuth;
    private final java.util.List<String> ingredients;
    private final int expectedStatusCode;
    private final String expectedMessage;
    private final boolean expectedSuccess;

    public ParametrizedOrderTest(String testName, boolean useAuth,
                                 java.util.List<String> ingredients,
                                 int expectedStatusCode, String expectedMessage,
                                 boolean expectedSuccess) {
        this.testName = testName;
        this.useAuth = useAuth;
        this.ingredients = ingredients;
        this.expectedStatusCode = expectedStatusCode;
        this.expectedMessage = expectedMessage;
        this.expectedSuccess = expectedSuccess;
    }

    @Test
    @DisplayName("Параметризированный тест создания заказа")
    public void parametrizedCreateOrderTest() {
        Order order = new Order();

        if (ingredients != null && ingredients.contains("valid_ingredient_will_be_replaced")) {
            order.setIngredients(Collections.singletonList(validIngredientId));
        } else {
            order.setIngredients(ingredients);
        }

        Response response;
        if (useAuth) {
            // ПРАВИЛЬНО: через клиента
            response = clients.OrderClient.createOrder(order, accessToken);
        } else {
            // ПРАВИЛЬНО: через клиента
            response = clients.OrderClient.createOrderWithoutAuth(order);
        }

        response.then()
                .statusCode(expectedStatusCode);

        if (expectedMessage != null) {
            response.then()
                    .body("message", equalTo(expectedMessage));
        }

        if (expectedStatusCode != INTERNAL_SERVER_ERROR) {
            response.then()
                    .body("success", equalTo(expectedSuccess));
        }
    }
}