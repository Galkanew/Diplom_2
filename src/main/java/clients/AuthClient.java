package clients;

import constants.ApiEndpoints;
import io.restassured.response.Response;
import models.User;
import static io.restassured.RestAssured.given;

public class AuthClient {

    public static Response login(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(ApiEndpoints.LOGIN);
    }

    public static String getAccessToken(User user) {
        return login(user)
                .then()
                .extract()
                .path("accessToken");
    }
}