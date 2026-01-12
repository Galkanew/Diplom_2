package api;

import helpers.DataGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.Order;
import models.User;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;

import java.util.Arrays;

import static steps.OrderSteps.checkAuthorizationRequiredError;
import static steps.OrderSteps.checkOrderCreatedSuccessfully;
import static steps.OrderSteps.createOrderWithAuth;
import static steps.OrderSteps.createOrderWithoutAuth;

public class OrderApiTest extends BaseTest {

    private String accessToken;

    @Before
    public void setUp() {
        User registeredUser = DataGenerator.getRandomUser();
        clients.UserClient.createUser(registeredUser);
        accessToken = clients.AuthClient.getAccessToken(registeredUser);
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderWithAuthSuccess() {
        String ingredientId = clients.OrderClient.getFirstIngredientId();
        Order order = new Order();
        order.setIngredients(Arrays.asList(ingredientId));

        Response response = createOrderWithAuth(order, accessToken);
        checkOrderCreatedSuccessfully(response);
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuthError() {
        String ingredientId = clients.OrderClient.getFirstIngredientId();
        Order order = new Order();
        order.setIngredients(Arrays.asList(ingredientId));

        Response response = createOrderWithoutAuth(order);
        checkAuthorizationRequiredError(response);
    }
}