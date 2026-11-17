package io.github.vageesha92.api;

import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CustomerApiTest extends BaseApiTest {

    private static final int CUSTOMER_ID = 12212;   // demo customer "john"

    @Test
    public void getCustomerReturnsJohnAsJson() {
        given(ApiSpec.bank())
                .when()
                .get("/customers/{id}", CUSTOMER_ID)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(CUSTOMER_ID));
    }
}
