package io.github.vageesha92.api;

import org.testng.annotations.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AccountApiTest extends BaseApiTest {

    private static final long JOHN = 12212;
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
}
