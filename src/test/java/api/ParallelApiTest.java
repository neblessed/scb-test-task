package api;

import config.Config;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static io.restassured.RestAssured.given;


//Создал для теста CleanURL, не успел доделать.
// В доке написано 'Limit 2 requests per second'

@Execution(ExecutionMode.CONCURRENT)
public class ParallelApiTest extends Config {

    @Test
    public void firstRequest(){
        given()
                .when()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"url\":\"https://www.google.com\"\n" +
                        "    }")
                .post(CLEAN_UI_URL)
                .then()
                .statusCode(200);
    }

    @Test
    public void secondRequest(){
        given()
                .when()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"url\":\"https://www.vk.com\"\n" +
                        "    }")
                .post(CLEAN_UI_URL)
                .then()
                .statusCode(200);
    }

    @Test
    public void thirdRequest(){
        given()
                .when()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"url\":\"https://www.gmail.com\"\n" +
                        "    }")
                .post(CLEAN_UI_URL)
                .then()
                .statusCode(200);
    }
}
