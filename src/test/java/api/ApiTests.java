package api;

import api.pojo.RandomUserPojo;
import configs.Config;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.UpperCase;
import org.junit.runner.Runner;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ApiTests extends Config {
    @ParameterizedTest
    @CsvFileSource(resources = "/test_data/dataForPositiveTests.csv")
    @DisplayName("CleanURL - Отправка валидного body")
    public void sendRightUrls(String url) {
        Response response = given()
                .when()
                .contentType(ContentType.JSON)
                .body(url)
                .post(CLEAN_UI_URL)
                .then()
                .statusCode(200)
                //.time(lessThan(500L))
                .extract().response();
        Assertions.assertThat(response.jsonPath()
                        .getString("result_url"))
                .startsWith("https://cleanuri.com/");
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/test_data/dataForNegativeTests.csv")
    @DisplayName("CleanURL - Отправка невалидного body")
    public void sendWrongUrls(String url) {
        Response response = given()
                .when()
                .contentType(ContentType.JSON)
                .body(url)
                .post(CLEAN_UI_URL)
                .then()
                .statusCode(400)
                .extract().response();
        Assertions.assertThat(response.jsonPath()
                        .getString("error"))
                .startsWith("API Error:");
    }

    @Test
    @DisplayName("RandomUserApi - Проверка статус кода")
    public void checkStatusCode() {
        given()
                .when()
                .contentType(ContentType.JSON)
                .get(RANDOM_USER)
                .then()
                .statusCode(200);
    }

    @Test
    @DisplayName("RandomUserApi - Получение записей с конкретным Gender")
    public void checkQueryParams() {
        List<RandomUserPojo> users = given()
                .when()
                .contentType(ContentType.JSON)
                .queryParam("gender", "male")
                .get(RANDOM_USER)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("results", RandomUserPojo.class);
        assertTrue(users.stream().allMatch(x -> x.getGender().equals("male")));
    }

    @Test
    @DisplayName("RandomUserApi - Получение списка с ограничением на количество записей")
    public void checkQueryParamsLimiter() {
        int resultsQuantity = 17;
        List<RandomUserPojo> users = given()
                .when()
                .contentType(ContentType.JSON)
                .queryParam("results", resultsQuantity)
                .get(RANDOM_USER)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("results", RandomUserPojo.class);
        assertEquals(resultsQuantity, users.size());
    }

    @Test
    @DisplayName("RandomUserApi - Получение >5000 пользователей")
    public void getRandomUsersOverLimit() {
        //При привышении лимита сервер возвращает 1 пользователя
        List<RandomUserPojo> users = given()
                .when()
                .contentType(ContentType.JSON)
                .queryParam("results", 10000)
                .get(RANDOM_USER)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("results", RandomUserPojo.class);
        Assertions.assertThat(users.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("RandomUserApi - Получение 5000 пользователей")
    public void getMaximumRandomUsers() {
        int resultQuantity = 5000;
        List<RandomUserPojo> users = given()
                .when()
                .contentType(ContentType.JSON)
                .queryParam("results", resultQuantity)
                .get(RANDOM_USER)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("results", RandomUserPojo.class);
        Assertions.assertThat(users.size()).isEqualTo(resultQuantity);
    }

    @Test
    @DisplayName("RandomUserApi - Установление условия для пароля")
    public void setUpUserPassword() {
        List<RandomUserPojo> users = given()
                .when()
                .contentType(ContentType.JSON)
                .queryParam("password", "upper")
                .get(RANDOM_USER)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("results.login", RandomUserPojo.class);
        Assertions.assertThat(users.get(0).getPassword()).isUpperCase();
    }

    @Test
    @DisplayName("RandomUserApi - Путь к фото профиля соответствует полу юзера")
    public void checkMatchGenderAndPictureEndpoint() {
        List<RandomUserPojo> users = given()
                .when()
                .contentType(ContentType.JSON)
                .queryParam("gender", "female")
                .queryParam("results", 1)
                .get(RANDOM_USER)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("results.picture", RandomUserPojo.class);
        Assertions.assertThat(users.get(0).getThumbnail()).contains("women");
    }

    @Test
    @DisplayName("RandomUserApi - Email заканчивается на @example.com")
    public void checkEmailEndsWith() {
        List<RandomUserPojo> users = given()
                .when()
                .contentType(ContentType.JSON)
                .queryParam("results", 100)
                .get(RANDOM_USER)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("results", RandomUserPojo.class);
        assertTrue(users.stream().allMatch(x -> x.getEmail().endsWith("@example.com")));
    }

    @Test
    @DisplayName("RandomUserApi - Один и тот же user при использовании seed")
    public void getUserBySeed() {
        List<RandomUserPojo> users = given()
                .when()
                .contentType(ContentType.JSON)
                .queryParam("seed", "test")
                .queryParam("results", 1)
                .get(RANDOM_USER)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("results.name", RandomUserPojo.class);
        Assertions.assertThat(users.get(0).getTitle()).isEqualTo("Miss");
        Assertions.assertThat(users.get(0).getFirst()).isEqualTo("Areta");
        Assertions.assertThat(users.get(0).getLast()).isEqualTo("Araújo");
    }

    @Test
    @DisplayName("RandomUserApi - Проверка зависимости адреса и национальности пользователя")
    public void checkMatchingNationalityAndAddress() {
        String nationality = "DE";
        List<RandomUserPojo> users = given()
                .when()
                .contentType(ContentType.JSON)
                .queryParam("nat", nationality)
                .get(RANDOM_USER)
                .then()
                .statusCode(200)
                .extract().body().jsonPath().getList("results.location", RandomUserPojo.class);
        assertTrue(users.stream().allMatch(x -> x.getCountry().equals("Germany")));
    }
}
