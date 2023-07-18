package com.example.shop;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

public class ShopApplicationUiTest extends BaseTestUi {

    MainPageUiData mainPageUiData = new MainPageUiData();

    @Test
    @Order(1)
    @DisplayName("Проверка создания магазина")
    @Disabled
    public void shouldCreateShop() {mainPageUiData.shouldCreateShop(); }

    @Test
    @Order(2)
    @DisplayName("Проверка удаления магазина")
    @Disabled
    public void shouldDeleteShop() {
        mainPageUiData.shouldDeleteShop(mainPageUiData.shouldCreateShop());
    }

    @Test
    @Order(3)
    @DisplayName("Проверка перехода по ссылкам")
    public void shouldButtonInFooter() {
        mainPageUiData.shouldButtonInFooter();
    }
}
