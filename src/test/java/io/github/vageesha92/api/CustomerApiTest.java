package io.github.vageesha92.api;

import io.github.vageesha92.core.Config;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.net.URI;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CustomerApiTest {

    private static final int CUSTOMER_ID = 12212;   // demo customer "john"

    @BeforeSuite
    public void seedDatabase() {
        URI api = Config.get().apiBaseUri();
        RestAssured.baseURI = api.getScheme() + "://" + api.getHost() + ":" + api.getPort();
        RestAssured.basePath = api.getPath();

        given()
                .when()
                .post("/initializeDB")
                .then()
                .statusCode(204);
    }

    @Test
    public void getCustomerReturnsJohnAsJson() {
        given()
                .accept(ContentType.JSON)               // ParaBank sends XML unless we ask for JSON
                .when()
                .get("/customers/{id}", CUSTOMER_ID)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(CUSTOMER_ID));
    }
}