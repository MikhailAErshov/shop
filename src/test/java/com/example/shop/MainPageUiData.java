package com.example.shop;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Selenide.*;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

public class MainPageUiData {
    private SelenideElement title = $x("/html/head/title");
    private SelenideElement linkCreateShop = $x("//div[@id='links']/a[contains(text(),'Create shop')]");
    private SelenideElement linkAllShop = $x("//div[@id='links']/a[contains(text(),'All shops')]");
    private SelenideElement linkDeleteShop = $x("//div[@id='links']/a[contains(text(),'Delete shop')]");
    private SelenideElement buttonRefresh = $x("//div[@id='shops_div']/button");
    private ElementsCollection responseTable = $$x("//tbody[@id='response']");
    private ElementsCollection shopsId = $$x("//tbody[@id='response']//td[1]");
    private ElementsCollection responseTableId = $$x("//*[@id='response']/tr/td[1]");
    private ElementsCollection responseTableName = $$x("//*[@id='response']/tr/td[2]");
    private ElementsCollection responseTablePublic = $$x("//*[@id='response']/tr/td[3]");
    private SelenideElement inputShopName = $x("//input[@id='name']");
    private SelenideElement inputCheckboxPublic = $x("//input[@id='public']");
    private SelenideElement buttonCreateShop = $x("//div[@id='create']/div/button");
    private SelenideElement inputDeleteId = $x("//input[@id='id']");
    private SelenideElement buttonDeleteShop = $x("//div[@id='delete']/div/button");
    private ElementsCollection errorValidationName = $$x("//*[@id='name_validation']/ul/li");
    private SelenideElement errorValidationDelete = $x("//*[@id='id_validation']/p");
    private SelenideElement footerLinkTelegram = $x("//footer/div/a[1]");
    private SelenideElement footerLinkVk = $x("//footer/div/a[2]");

    private List<String> getTextFromElements(ElementsCollection elements){
        List<String> textElements = new ArrayList<>();
        elements.asDynamicIterable().forEach(product -> {
            String[] strings = product.getText().split("\\n");
            for (String s : strings
            ) {
                textElements.add(s);
            }
        });
        return textElements;
    }

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

//        List<String> errorList = getTextFromElements(errorValidationName);

        step("Проверить валидацию полей", () -> {
            errorValidationName.should(CollectionCondition
                    .exactTextsCaseSensitiveInAnyOrder("Name should begin with a capital letter.",
                            "Name length should be more than 6 characters."));
        });

//        List<String> searchShopResultBeforeCreate = shopsId.texts();

        List<ShopModelUI> shopsBefore = new ArrayList<>();
        for (int i = 0; i < responseTableId.size(); i++) {
            for (int i1 = 0; i1 < responseTableName.size(); i1++) {
                for (int i2 = 0; i2 < responseTablePublic.size(); i2++) {
                    shopsBefore.add(new ShopModelUI(responseTableId.get(i).getText(), responseTableName.get(i1).getText(),
                            responseTablePublic.get(i2).getText()));
                }
            }
        }

//        List<ShopModelUI> shopsBefore2 = responseTableId.stream()
//                .flatMap(id -> responseTableName.stream()
//                .flatMap(name -> responseTablePublic.stream()
//                .map(isPublic -> new ShopModelUI(id.getText(), name.getText(), isPublic.getText()))))
//                .toList();

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
            buttonRefresh.shouldBe(Condition.visible, Duration.ofSeconds(5));
            buttonRefresh.hover().click();
            assertEquals("Refresh", buttonRefresh.getText());
        });

        sleep(10000);

//        List<String> searchShopResultAfterCreate = shopsId.texts();

        List<ShopModelUI> shopsAfter = new ArrayList<>();
        for (int i = 0; i < responseTableId.size(); i++) {
            for (int i1 = 0; i1 < responseTableName.size(); i1++) {
                for (int i2 = 0; i2 < responseTablePublic.size(); i2++) {
                    shopsAfter.add(new ShopModelUI(responseTableId.get(i).getText(), responseTableName.get(i1).getText(),
                            responseTablePublic.get(i2).getText()));
                }
            }
        }

//        List<ShopModelUI> shopsAfter2 = responseTableId.stream()
//                .flatMap(id -> responseTableName.stream()
//                        .flatMap(name -> responseTablePublic.stream()
//                                .map(isPublic -> new ShopModelUI(id.getText(), name.getText(), isPublic.getText()))))
//                .toList();

//        searchShopResultAfterCreate.removeAll(searchShopResultBeforeCreate);

        shopsAfter.removeAll(shopsBefore);

        step("Убедиться что магазин создан и есть в списке", () -> {
            assertEquals(newShopName, shopsAfter.get(0).getShopName());
            assertEquals("true", shopsAfter.get(0).getIsPublic());
        });

        return shopsAfter.get(0).getId();
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
        responseTable.forEach(product -> {
            String[] strings = product.getText().split("\\n");
            for (String s : strings
            ) {
                searchShopResultBeforeDelete.add(s);
            }
        });

        step("Заполнить поле id магазина", () -> {
            inputDeleteId.shouldBe(Condition.visible);
            inputDeleteId.sendKeys(shopId);
        });

        step("Нажать кнопку удалить магазин", () -> {
            buttonDeleteShop.click();
        });

        step("Обновить список магазинов", () -> {
            buttonRefresh.hover().click();
            assertEquals("Refresh", buttonRefresh.getText());
        });

        sleep(10000);

        List<String> searchShopResultAfterDelete = new ArrayList<>();
        while (searchShopResultAfterDelete.isEmpty()) {
            responseTable.forEach(product -> {
                String[] strings = product.getText().split("\\n");
                for (String s : strings
                ) {
                    searchShopResultAfterDelete.add(s);
                }
            });
        }

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
