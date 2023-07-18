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
        open("D:\\QA\\projects\\shopatt\\src\\main\\java\\com\\example\\shop\\ui\\main.html");
    }

     @AfterAll
    public static void tearDown() {
        Selenide.closeWebDriver();
    }
}
