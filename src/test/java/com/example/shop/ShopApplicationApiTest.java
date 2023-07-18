package com.example.shop;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

public class ShopApplicationApiTest extends BaseTestApi {

    @BeforeAll
    public static void setUpRequest() {
        requestSpecificationForTest(requestSpecification());
    }

    @Test
    @DisplayName("Создание магазина с валидными данными")
    public void addShop_shouldAddShop200Response() {
        String expectedShopName = "Magazin888";
        String expectedShopPublic = "true";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("shopName", expectedShopName);
        parameters.put("shopPublic", expectedShopPublic);

        given()
                .body(parameters)
                .when()
                .post("/add")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("Проверка названия с маленькой буквы")
    public void addShop_shouldAddShop400ResponseWhenNameDoesNotBeginWithCapitalLetter() {
        String expectedShopName = "magazin888";
        String expectedShopPublic = "true";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("shopName", expectedShopName);
        parameters.put("shopPublic", expectedShopPublic);

        String response = given()
                .body(parameters)
                .when()
                .post("/add")
                .then().log().all()
                .extract().response().getBody().asString();

        assertEquals("Name should begin with a capital letter", response);
    }

    @Test
    @DisplayName("Проверка короткого названия")
    public void addShop_shouldAddShop400ResponseWhenNameIsTooShort() {
        String expectedShopName = "Magaz";
        String expectedShopPublic = "true";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("shopName", expectedShopName);
        parameters.put("shopPublic", expectedShopPublic);

        String response = given()
                .body(parameters)
                .when()
                .post("/add")
                .then().log().all()
                .extract().response().getBody().asString();

        assertEquals("Name should be more than 6 letters", response);
    }

    @Test
    @DisplayName("Проверка получения списка магазинов")
    public void getShops_shouldReturnListOfShop() {

        given()
                .contentType(ContentType.JSON)
                .get("/all")
                .then()
                .assertThat().body(matchesJsonSchemaInClasspath("schema.json"))
                .statusCode(200);
    }

    @Test
    @DisplayName("Получение магазина по идентификатору")
    public void getShop_shouldReturnShopByIdAnd200Response() {

        String expectedShopName = "Magazin555";
        String expectedShopPublic = "true";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("shopName", expectedShopName);
        parameters.put("shopPublic", expectedShopPublic);

        Response responseBefore = given()
                .contentType(ContentType.JSON)
                .get("/all")
                .then().log().all()
                .extract().response();

        given()
                .body(parameters)
                .when()
                .post("/add")
                .then().log().all()
                .statusCode(200);

        Response responseAfter = given()
                .contentType(ContentType.JSON)
                .get("/all")
                .then().log().all()
                .extract().response();

        JsonPath jsonPathBefore = responseBefore.jsonPath();
        JsonPath jsonPathAfter = responseAfter.jsonPath();

        List<String> listBefore = jsonPathBefore.getList("shopId");
        List<String> listAfter = jsonPathAfter.getList("shopId");

        listAfter.removeAll(listBefore);

        String expectedShopId = (listAfter.toString()
                .replace("[", "").replace("]", ""));

        given()
                .contentType(ContentType.JSON)
                .get("/" + expectedShopId)
                .then().log().all()
                .statusCode(200)
                .body("shopName", equalTo(expectedShopName))
                .body("shopPublic", equalTo(true));
    }

    @Test
    @DisplayName("Удаление магазина по идентификатору")
    public void deleteShop_shouldDeleteShopByIdAnd204Response() {

        String expectedShopName = "Magazin555";
        String expectedShopPublic = "true";

        Map<String, String> parameters = new HashMap<>();
        parameters.put("shopName", expectedShopName);
        parameters.put("shopPublic", expectedShopPublic);

        Response responseBefore = given()
                .contentType(ContentType.JSON)
                .get("/all")
                .then().log().all()
                .extract().response();

        given()
                .body(parameters)
                .when()
                .post("/add")
                .then().log().all()
                .statusCode(200);

        Response responseAfter = given()
                .contentType(ContentType.JSON)
                .get("/all")
                .then().log().all()
                .extract().response();

        JsonPath jsonPathBefore = responseBefore.jsonPath();
        JsonPath jsonPathAfter = responseAfter.jsonPath();

        List<String> listBefore = jsonPathBefore.getList("shopId");
        List<String> listAfter = jsonPathAfter.getList("shopId");

        listAfter.removeAll(listBefore);

        String expectedShopId = (listAfter.toString()
                .replace("[", "").replace("]", ""));

        given()
                .contentType(ContentType.JSON)
                .delete("/delete/" + expectedShopId)
                .then().log().all()
                .statusCode(204);

        Response responseAfterDelete = given()
                .contentType(ContentType.JSON)
                .get("/all")
                .then().log().all()
                .extract().response();

        JsonPath jsonPathAfterDelete = responseAfterDelete.jsonPath();

        List<String> listAfterDelete = jsonPathAfterDelete.getList("shopId");

        assertEquals(listBefore, listAfterDelete);
    }

    @Test
    @DisplayName("Проверка удаления без идентификатора")
    public void deleteShop_shouldDeleteShopByNoNumberIdAnd404Response() {
        String invalidId = "";
        given()
                .contentType(ContentType.JSON)
                .delete("/delete/" + invalidId)
                .then().log().all()
                .statusCode(404)
                .body("error", equalTo("Not Found"));
    }
}
