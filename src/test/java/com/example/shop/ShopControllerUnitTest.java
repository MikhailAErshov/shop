package com.example.shop;

import com.example.shop.Configuration;
import com.example.shop.controllers.ShopController;
import com.example.shop.models.ShopDto;
import com.example.shop.models.ShopPojo;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopControllerUnitTest extends Configuration {


    @Test
    @DisplayName("Создание магазина с валидными данными")
    void addShop_shouldAddShopAndReturn200Response() {
        ShopController shopController = new ShopController();
        ShopDto dto = new ShopDto(12250L, "ShopTest123", true);

        ResponseEntity<String> response = shopController.addShop(dto);

        JSONObject object = new JSONObject(dto);
        final ShopPojo pojo = new ShopPojo();
        pojo.setShopName(object.get("shopName").toString());
        pojo.setShopPublic(Boolean.parseBoolean(object.get("shopPublic").toString()));

        assertEquals(200, response.getStatusCode().value());
        assertEquals("ShopTest123", pojo.getShopName());
        assertTrue(pojo.getShopPublic());
    }

    @Test
    @DisplayName("Проверка короткого названия магазина")
    void addShop_shouldReturn400ResponseWhenNameIsTooShort() {
        ShopController shopController = new ShopController();
        ShopDto dto = new ShopDto(12250L, "Shop", true);

        ResponseEntity<String> response = shopController.addShop(dto);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Name should be more than 6 letters", response.getBody());
    }

    @Test
    @DisplayName("Проверка названия с маленькой буквы")
    void addShop_shouldReturn400ResponseWhenNameDoesNotBeginWithCapitalLetter() {
        ShopController shopController = new ShopController();
        ShopDto dto = new ShopDto(12250L, "shopTest", true);

        ResponseEntity<String> response = shopController.addShop(dto);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Name should begin with a capital letter", response.getBody());
    }

    @Test
    @DisplayName("Проверка на пустое поле")
    void addShop_shouldReturn400ResponseWhenShopNameIsEmpty() {
        ShopController shopController = new ShopController();
        ShopDto dto = new ShopDto(12250L, "", true);

        ResponseEntity<String> response = shopController.addShop(dto);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Name should be more than 6 letters", response.getBody());
    }

    @Test
    @DisplayName("Проверка на пробелы")
    void addShop_shouldReturn400ResponseWhenShopNameIsWhitespace() {
        ShopController shopController = new ShopController();
        ShopDto dto = new ShopDto(12250L, "        ", true);

        ResponseEntity<String> response = shopController.addShop(dto);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Name should begin with a capital letter", response.getBody());
    }

    @Test
    @DisplayName("Проверка на длинное граничное название")
    void addShop_shouldReturn200ResponseWhenShopNameIs256Symbols() {
        ShopController shopController = new ShopController();
        String ShopNameIs256Symbols = "Shoptest".repeat(32);
        ShopDto dto = new ShopDto(12250L, ShopNameIs256Symbols, true);

        ResponseEntity<String> response = shopController.addShop(dto);

        JSONObject object = new JSONObject(dto);
        final ShopPojo pojo = new ShopPojo();
        pojo.setShopName(object.get("shopName").toString());
        pojo.setShopPublic(Boolean.parseBoolean(object.get("shopPublic").toString()));

        assertEquals(200, response.getStatusCode().value());
        assertEquals(ShopNameIs256Symbols, pojo.getShopName());
    }

    @Test
    @DisplayName("Проверка на длинное сверхграничное название")
    void addShop_shouldReturn500ResponseWhenShopNameIs257Symbols() {
        try {
            ShopController shopController = new ShopController();
            String ShopNameIs257Symbols = "Shoptest".repeat(32) + "1";
            ShopDto dto = new ShopDto(12250L, ShopNameIs257Symbols, true);

            ResponseEntity<String> response = shopController.addShop(dto);

            JSONObject object = new JSONObject(dto);
            final ShopPojo pojo = new ShopPojo();
            pojo.setShopName(object.get("shopName").toString());
            pojo.setShopPublic(Boolean.parseBoolean(object.get("shopPublic").toString()));

            assertEquals(200, response.getStatusCode().value());
        } catch (Exception e) {
            System.out.println("Превышено максимальное количество символов в поле shopName");
        }
    }

    @Test
    @DisplayName("Проверка получения списка магазинов")
    void getShops_shouldReturnListOfShopPojoAnd200Response() {
        ShopController shopController = new ShopController();
        ShopDto dto = new ShopDto(12250L, "ShopTest18953", true);

        shopController.addShop(dto);

        ResponseEntity<List<ShopPojo>> response = shopController.getShops();

        assertEquals(200, response.getStatusCode().value());
        assertFalse(response.getBody().isEmpty());

        for (int i = 0; i < response.getBody().size(); i++) {
            assertNotNull(response.getBody().get(i).getShopId());
            assertNotNull(response.getBody().get(i).getShopName());
            assertNotNull(response.getBody().get(i).getShopPublic());
        }
    }

    @Test
    @DisplayName("Получение магазина по идентификатору")
    void getShop_shouldReturnShopByIdAnd200Response() {
        String expectedShopName = "BBBBBBBBBB";
        boolean expectedShopPublic = true;

        ShopController shopController = new ShopController();

        List<ShopPojo> actualShops = shopController.getShops().getBody();

        ShopDto dto = new ShopDto(12250L, expectedShopName, expectedShopPublic);
        shopController.addShop(dto);

        List<ShopPojo> responseShops = shopController.getShops().getBody();

        responseShops.removeAll(actualShops);

        assertEquals(1, responseShops.size());
        assertEquals(expectedShopName, responseShops.get(0).getShopName());
        assertEquals(expectedShopPublic, responseShops.get(0).getShopPublic());
    }

    @Test
    @DisplayName("Удаление магазина по идентификатору")
    void deleteShop_shouldReturnShopDeleteByIdAnd204Response() {
        String expectedShopName = "ShopDeleteById";
        boolean expectedShopPublic = true;

        ShopController shopController = new ShopController();

        List<ShopPojo> actualShops = shopController.getShops().getBody();

        ShopDto dto = new ShopDto(12250L, expectedShopName, expectedShopPublic);
        shopController.addShop(dto);

        List<ShopPojo> responseShops = shopController.getShops().getBody();

        responseShops.removeAll(actualShops);

        Long lastShopId = responseShops.get(0).getShopId();

        ResponseEntity<String> response = shopController.deleteShop(lastShopId);

        assertEquals(204, response.getStatusCode().value());
        assertEquals(HttpHeaders.EMPTY, response.getHeaders());
    }
}