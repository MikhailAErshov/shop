package com.example.shop;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

public class ShopApplicationUiTest extends BaseTestUi {

    MainPageUiData mainPageUiData = new MainPageUiData();

    public static String shopId;

    @Test
    @Order(1)
    @DisplayName("Проверка создания магазина")
    public void shouldCreateShop() {
        shopId = mainPageUiData.shouldCreateShop();
    }

    @Test
    @Order(2)
    @DisplayName("Проверка удаления магазина")
    public void shouldDeleteShop() {
        mainPageUiData.shouldDeleteShop(shopId);
    }

    @Test
    @Order(3)
    @DisplayName("Проверка перехода по ссылкам")
    public void shouldButtonInFooter() {
        mainPageUiData.shouldButtonInFooter();
    }
}
