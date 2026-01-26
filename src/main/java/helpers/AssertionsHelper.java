package helpers;

import constants.ErrorMessages;
import io.restassured.response.Response;
import static org.junit.Assert.assertEquals;

public class AssertionsHelper {

    public static void assertStatusCode(Response response, int expectedStatusCode) {
        assertEquals(
                ErrorMessages.STATUS_CODE_NOT_EXPECTED,
                expectedStatusCode,
                response.getStatusCode()
        );
    }

    public static void assertFieldEquals(Response response, String jsonPath, Object expectedValue) {
        assertEquals(
                ErrorMessages.FIELD_NOT_EQUAL,
                expectedValue,
                response.path(jsonPath)
        );
    }
}