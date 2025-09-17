package AldiAPITests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

    @BeforeAll
    public static void setup() {
        // Set the base URI for all tests
        RestAssured.baseURI = "https://api.aldi.us"; // Replace with your actual API base URL
    }
}
