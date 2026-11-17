package io.github.vageesha92.api;

import org.testng.annotations.BeforeSuite;

import static io.restassured.RestAssured.given;

public abstract class BaseApiTest {

    // resets the demo data (john, 12212) - not safe in a parallel run
    @BeforeSuite
    public void resetTheBank() {
        given(ApiSpec.bank())
                .post("/initializeDB")
                .then().statusCode(204);
    }
}
