package io.github.vageesha92.api;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;

public class AccountClient {

    private final RequestSpecification spec = ApiSpec.bank();

    public Response accountsOf(long customerId) {
        return given(spec).get("/customers/{id}/accounts", customerId);
    }

    public Response account(long accountId) {
        return given(spec).get("/accounts/{id}", accountId);
    }

    public BigDecimal balanceOf(long accountId) {
        return account(accountId).then().statusCode(200).extract().as(Account.class).balance();
    }
}
