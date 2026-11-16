package io.github.vageesha92.ui;

import io.github.vageesha92.core.Config;
import io.github.vageesha92.core.DriverManager;
import io.github.vageesha92.ui.pages.AccountsOverviewPage;
import io.github.vageesha92.ui.pages.AccountsOverviewPage.AccountRow;
import io.github.vageesha92.ui.pages.OpenAccountPage;
import io.github.vageesha92.ui.pages.RegisterPage;
import io.github.vageesha92.ui.pages.TransferFundsPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.stream.Collectors;

import static org.testng.Assert.assertEquals;

public class TransferFundsTest extends BaseUiTest {

    @Test
    public void transferMovesMoneyBetweenTwoAccounts() {
        WebDriver driver = DriverManager.getDriver();
        String base = Config.get().uiBaseUrl();
        new RegisterPage(driver).open(base).register(RegisterPage.uniqueUsername(), "secret1");
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

    }

    private static Map<String, BigDecimal> balances(AccountsOverviewPage overview) {
        return overview.accounts().stream().collect(Collectors.toMap(AccountRow::id, AccountRow::balance));
    }
}
