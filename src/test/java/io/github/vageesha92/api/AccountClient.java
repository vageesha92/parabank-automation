package io.github.vageesha92.api;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;

public class AccountClient {

    // account types in ParaBank: 0 = CHECKING, 1 = SAVINGS, 2 = LOAN
    public static final int CHECKING = 0;
    public static final int SAVINGS = 1;

    private final RequestSpecification spec = ApiSpec.bank();

    public Response accountsOf(long customerId) {
        return given(spec).get("/customers/{id}/accounts", customerId);
    }

    public Response account(long accountId) {
        return given(spec).get("/accounts/{id}", accountId);
    }

    // ParaBank takes the opening deposit from fromAccountId
    public Response createAccount(long customerId, int newAccountType, long fromAccountId) {
        return given(spec)
                .queryParam("customerId", customerId)
                .queryParam("newAccountType", newAccountType)
                .queryParam("fromAccountId", fromAccountId)
                .post("/createAccount");
    }

    public Response transfer(long fromAccountId, long toAccountId, BigDecimal amount) {
        return given(spec)
                .queryParam("fromAccountId", fromAccountId)
                .queryParam("toAccountId", toAccountId)
                .queryParam("amount", amount)
                .post("/transfer");
    }

    public BigDecimal balanceOf(long accountId) {
        return account(accountId).then().statusCode(200).extract().as(Account.class).balance();
    }
}
