package com.example.shop;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

public class MainPageUiData {
    public SelenideElement title = $x("/html/head/title");
    public SelenideElement linkCreateShop = $x("//div[@id='links']/a[contains(text(),'Create shop')]");
    public SelenideElement linkAllShop = $x("//div[@id='links']/a[contains(text(),'All shops')]");
    public SelenideElement linkDeleteShop = $x("//div[@id='links']/a[contains(text(),'Delete shop')]");
    public SelenideElement buttonRefresh = $x("//div[@id='shops_div']/button");
    public ElementsCollection responseTable = $$x("//tbody[@id='response']");
    public SelenideElement inputShopName = $x("//input[@id='name']");
    public SelenideElement inputCheckboxPublic = $x("//input[@id='public']");
    public SelenideElement buttonCreateShop = $x("//div[@id='create']/div/button");
    public SelenideElement inputDeleteId = $x("//input[@id='id']");
    public SelenideElement buttonDeleteShop = $x("//div[@id='delete']/div/button");
    public ElementsCollection errorValidationName = $$x("//*[@id='name_validation']/ul");
    public SelenideElement errorValidationDelete = $x("//*[@id='id_validation']/p");
    public SelenideElement footerLinkTelegram = $x("//footer/div/a[1]");
    public SelenideElement footerLinkVk = $x("//footer/div/a[2]");

    public String shouldCreateShop() {

        step("Успешно открывается сайт, кнопки в хедере отображаются", () -> {
            title.shouldHave(attribute("text", "Create your own shop"));
            linkCreateShop.shouldBe(Condition.visible);
            linkAllShop.shouldBe(Condition.visible);
            linkDeleteShop.shouldBe(Condition.visible);
        });

        step("Перейти с кнопки в хедере для создания магазина", () -> {
            linkCreateShop.hover().click();
        });

        step("Проверить создание магазина с невалидными данными", () -> {
            buttonCreateShop.shouldBe(Condition.visible);
            buttonCreateShop.click();
        });

        List<String> errorList = new ArrayList<>();
        errorValidationName.forEach(error -> errorList.add(error.getText()));

        step("Проверить валидацию полей", () -> {
            assertTrue(errorList.contains("Name should begin with a capital letter."));
            assertTrue(errorList.contains("Name length should be more than 6 characters."));
        });

        List<String> searchShopResultBeforeCreate = new ArrayList<>();
        responseTable.forEach(product -> searchShopResultBeforeCreate.add(product.getText()));
        String newShopName = "MagazZZzin35";

        step("Заполнить поле названия магазина", () -> {
            inputShopName.shouldBe(Condition.visible);
            inputShopName.click();
            inputShopName.sendKeys(newShopName);
        });

        step("Установить чекбокс приватности магазина", () -> {
            inputCheckboxPublic.shouldBe(Condition.visible);
            inputCheckboxPublic.click();
        });

        step("Нажать кнопку добавления магазина", () -> {
            buttonCreateShop.shouldBe(Condition.visible);
            buttonCreateShop.click();
        });

        step("Обновить список магазинов через кнопку рефреш", () -> {
            buttonRefresh.shouldBe(Condition.visible);
            buttonRefresh.click();
        });

        List<String> searchShopResultAfterCreate = new ArrayList<>();
        responseTable.forEach(product -> searchShopResultAfterCreate.add(product.getText()));
        searchShopResultAfterCreate.removeAll(searchShopResultBeforeCreate);

        step("Убедиться что магазин создан и есть в списке", () -> {
            assertTrue(searchShopResultAfterCreate.contains(newShopName));
            assertEquals("true", searchShopResultAfterCreate.get(2));
        });

        String newShopId = searchShopResultAfterCreate.get(0);
        return newShopId;
    }

    public void shouldDeleteShop(String shopId) {

        step("Перейти с кнопки в хедере для удаления магазина", () -> {
            linkDeleteShop.hover().click();
        });

        step("Проверить валидацию полей", () -> {
            buttonDeleteShop.shouldBe(Condition.visible);
            buttonDeleteShop.click();
            assertTrue(errorValidationDelete.getText().contains("Must be not empty"));
        });

        List<String> searchShopResultBeforeDelete = new ArrayList<>();
        responseTable.forEach(product -> searchShopResultBeforeDelete.add(product.getText()));

        step("Заполнить поле id магазина", () -> {
            inputDeleteId.shouldBe(Condition.visible);
            inputDeleteId.sendKeys(shopId);
        });

        step("Нажать кнопку удалить магазин", () -> {
            buttonDeleteShop.click();
        });

        step("Обновить список магазинов", () -> {
            buttonRefresh.click();
        });

        List<String> searchShopResultAfterDelete = new ArrayList<>();
        responseTable.forEach(product -> searchShopResultAfterDelete.add(product.getText()));
        searchShopResultAfterDelete.removeAll(searchShopResultBeforeDelete);

        step("Убедиться что магазин удален и его нет в списке", () -> {
            assertTrue(searchShopResultAfterDelete.isEmpty());
        });
    }

    public void shouldButtonInFooter() {

        step("Проверить переход на сайт телеграм", () -> {
            footerLinkTelegram.shouldBe(Condition.visible);
            footerLinkTelegram.click();
            title.shouldHave(attribute("text", "Telegram Web"));
        });

        back();

        step("Проверить переход на сайт вконтакте", () -> {
            footerLinkVk.shouldBe(Condition.visible);
            footerLinkVk.click();
            title.shouldHave(attribute("text", "ВКонтакте | Добро пожаловать"));
        });
    }
}
