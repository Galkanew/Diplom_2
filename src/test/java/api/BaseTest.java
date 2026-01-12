package api;

import constants.ApiEndpoints;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.BeforeClass;

public class BaseTest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = ApiEndpoints.BASE_URL;
        RestAssured.filters(
                new RequestLoggingFilter(),
                new ResponseLoggingFilter(),
                new AllureRestAssured()
        );
    }
}