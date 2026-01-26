package clients;

import constants.ApiEndpoints;
import io.restassured.response.Response;
import models.Order;
import static io.restassured.RestAssured.given;

public class OrderClient {

    public static Response createOrder(Order order, String accessToken) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .post(ApiEndpoints.ORDERS);
    }

    public static Response createOrderWithoutAuth(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .post(ApiEndpoints.ORDERS);
    }

    public static Response getIngredients() {
        return given()
                .get(ApiEndpoints.INGREDIENTS);
    }

    public static String getFirstIngredientId() {
        return getIngredients()
                .then()
                .extract()
                .path("data[0]._id");
    }
}