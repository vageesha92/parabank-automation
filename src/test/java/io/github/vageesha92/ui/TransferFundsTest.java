package io.github.vageesha92.ui;

import io.github.vageesha92.api.AccountClient;
import io.github.vageesha92.api.WebSession;
import io.github.vageesha92.core.Config;
import io.github.vageesha92.core.DriverManager;
import io.github.vageesha92.db.Db;
import io.github.vageesha92.ui.pages.AccountsOverviewPage;
import io.github.vageesha92.ui.pages.AccountsOverviewPage.AccountRow;
import io.github.vageesha92.ui.pages.LoginPage;
import io.github.vageesha92.ui.pages.OpenAccountPage;
import io.github.vageesha92.ui.pages.RegisterPage;
import io.github.vageesha92.ui.pages.TransferFundsPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class TransferFundsTest extends BaseUiTest {

    @Test
    public void transferMovesMoneyBetweenTwoAccounts() throws SQLException {
        WebDriver driver = DriverManager.getDriver();
        String base = Config.get().uiBaseUrl();
        String user = RegisterPage.uniqueUsername();
        WebSession.register(user, "secret1");                                       // over HTTP, no browser
        new LoginPage(driver).useSession(base, WebSession.logIn(user, "secret1")); // skip the login form
        String second = new OpenAccountPage(driver).open(base).openAccount("SAVINGS");

        driver.get(base + "/overview.htm");
        Map<String, BigDecimal> before = balances(new AccountsOverviewPage(driver));
        String first = before.keySet().stream().filter(id -> !id.equals(second)).findFirst().orElseThrow();

        String result = new TransferFundsPage(driver).open(base).transfer("25", first, second);
        assertEquals(result, "Transfer Complete!");

        driver.get(base + "/overview.htm");

        Map<String, BigDecimal> after = balances(new AccountsOverviewPage(driver));

        assertEquals(
                after.get(first),
                before.get(first).subtract(new BigDecimal("25.00")),
                "Source account balance should decrease by 25"
        );

        assertEquals(
                after.get(second),
                before.get(second).add(new BigDecimal("25.00")),
                "Destination account balance should increase by 25"
        );

        // same balances through the API (JSON can say 415.5 where the page shows 415.50)
        AccountClient api = new AccountClient();
        assertEquals(api.balanceOf(Long.parseLong(first)).setScale(2), after.get(first), "API balance of account " + first);
        assertEquals(api.balanceOf(Long.parseLong(second)).setScale(2), after.get(second), "API balance of account " + second);

        assertEquals(newestTransaction(first), "Funds Transfer Sent 25.00");
        assertEquals(newestTransaction(second), "Funds Transfer Received 25.00");
    }

    private static Map<String, BigDecimal> balances(AccountsOverviewPage overview) {
        return overview.accounts().stream().collect(Collectors.toMap(AccountRow::id, AccountRow::balance));
    }

    private static String newestTransaction(String accountId) throws SQLException {
        String sql = "SELECT description, amount FROM Transaction WHERE account_id = ? ORDER BY id DESC";

        try (Connection db = Db.connect();
             PreparedStatement query = db.prepareStatement(sql)) {
            query.setLong(1, Long.parseLong(accountId));
            try (ResultSet rows = query.executeQuery()) {
                assertTrue(rows.next(), "No transaction found for account " + accountId);
                return rows.getString("description") + " " + rows.getBigDecimal("amount").setScale(2);
            }
        }
    }
}
