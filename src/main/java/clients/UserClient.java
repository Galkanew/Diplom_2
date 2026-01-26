package clients;

import constants.ApiEndpoints;
import io.restassured.response.Response;
import models.User;
import static io.restassured.RestAssured.given;

public class UserClient {

    public static Response createUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(ApiEndpoints.REGISTER);
    }
}