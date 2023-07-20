package com.example.shop;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeOptions;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import org.springframework.boot.SpringApplication;

import static com.codeborne.selenide.Condition.*;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

import static com.codeborne.selenide.Selenide.*;

public class BaseTestUi {

    @BeforeAll
    public static void setUpAll() {
        Configuration.browserSize = "1920x1080";
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void setUp() {
        Configuration.browserCapabilities = new ChromeOptions().addArguments("--remote-allow-origins=*");
        open("http://localhost:63342/shop/src/main/java/com/example/shop/ui/main.html?_ijt=93viesbf6gp5pt1cmss6j06ft4&_ij_reload=RELOAD_ON_SAVE");
        Selenide.webdriver().driver().getWebDriver().manage().addCookie(new Cookie("Idea-31a6112c", "55212b94-e383-41d7-aaee-e01e8d935910"));
        Selenide.webdriver().driver().getWebDriver().manage().addCookie(new Cookie("Idea-31a60d6b", "501bb3d3-3ecf-4d2e-87ab-c71256e25857"));
        refresh();
    }

     @AfterAll
    public static void tearDown() {
        Selenide.closeWebDriver();
    }
}
