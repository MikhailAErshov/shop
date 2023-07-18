package com.example.shop;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseTestApi {
    protected static final String baseUri = "http://localhost:4000/shops";

    public static RequestSpecification requestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(baseUri)
                .setContentType(ContentType.JSON)
                .build();
    }
    public static void requestSpecificationForTest(RequestSpecification requestSpecification){
        RestAssured.requestSpecification = requestSpecification;
    }
}
