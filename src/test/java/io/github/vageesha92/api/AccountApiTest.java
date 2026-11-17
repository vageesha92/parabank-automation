package io.github.vageesha92.api;

import org.testng.annotations.Test;

import java.math.BigDecimal;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AccountApiTest extends BaseApiTest {

    private static final long JOHN = 12212;
    private static final long FUNDED = 13122;   // john's account with 1100.00 after the reset
    private static final BigDecimal TEN = new BigDecimal("10.00");
    private final AccountClient accounts = new AccountClient();

    private Account[] johnsAccounts() {
        return accounts.accountsOf(JOHN).then().statusCode(200).extract().as(Account[].class);
    }

    @Test
    public void everyAccountInListBelongsToJohn() {
        Account[] list = johnsAccounts();
        assertTrue(list.length > 0, "john has no accounts after the reset");
        for (Account account : list) {
            assertEquals(account.customerId(), JOHN, "account " + account.id());
        }
    }

    @Test
    public void accountByIdMatchesAccountInList() {
        Account first = johnsAccounts()[0];
        Account byId = accounts.account(first.id())
                .then()
                .statusCode(200)
                .extract()
                .as(Account.class);

        assertEquals(byId, first);
    }

    @Test
    public void accountsMatchSchema() {
        accounts.accountsOf(JOHN).then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schemas/accounts.schema.json"));
    }

    @Test
    public void createAccountGivesJohnNewSavingsAccount() {
        Account created = accounts.createAccount(JOHN, AccountClient.SAVINGS, FUNDED)
                .then().statusCode(200).extract().as(Account.class);
        assertEquals(created.customerId(), JOHN);
        assertEquals(created.type(), "SAVINGS", "type 1 is a savings account");
    }

    @Test
    public void transferMovesMoneyOnServer() {
        long from = FUNDED;
        long to = accounts.createAccount(JOHN, AccountClient.CHECKING, from)
                .then().statusCode(200).extract().as(Account.class).id();
        BigDecimal fromBefore = accounts.balanceOf(from);
        BigDecimal toBefore = accounts.balanceOf(to);

        accounts.transfer(from, to, TEN).then().statusCode(200);

        // Compare with two decimals, because 415.5 and 415.50 are the same amount.
        assertEquals(accounts.balanceOf(from).setScale(2), fromBefore.subtract(TEN).setScale(2), "from account");
        assertEquals(accounts.balanceOf(to).setScale(2), toBefore.add(TEN).setScale(2), "to account");
    }
}
