package io.github.vageesha92.ui;

import io.github.vageesha92.core.Config;
import io.github.vageesha92.core.DriverManager;
import io.github.vageesha92.ui.pages.AccountsOverviewPage;
import io.github.vageesha92.ui.pages.AccountsOverviewPage.AccountRow;
import io.github.vageesha92.ui.pages.RegisterPage;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

public class AccountsOverviewTest extends BaseUiTest {

    @Test
    public void newCustomerHasOneAccountAndTotalAddsUp() {
        WebDriver driver = DriverManager.getDriver();
        String user = RegisterPage.uniqueUsername();
        String welcome = new RegisterPage(driver).open(Config.get().uiBaseUrl()).register(user, "secret1");
        assertEquals(welcome, "Welcome " + user);

        driver.get(Config.get().uiBaseUrl() + "/overview.htm");
        AccountsOverviewPage overview = new AccountsOverviewPage(driver);
        List<AccountRow> accounts = overview.accounts();

        assertEquals(accounts.size(), 1, "a new customer starts with one account: " + accounts);

        BigDecimal sum = accounts.stream().map(AccountRow::balance).reduce(BigDecimal.ZERO, BigDecimal::add);
        assertEquals(overview.total(), sum, "Total must equal the sum of account balances: " + accounts);

        // no AJAX call is still open once the rows are there
        assertEquals(overview.openAjaxCalls(), 0L, "no open AJAX calls after the Total row is shown");
    }

    @Test
    public void rowHeldOverReloadGoesStale() {
        WebDriver driver = DriverManager.getDriver();
        new RegisterPage(driver).open(Config.get().uiBaseUrl()).register(RegisterPage.uniqueUsername(), "secret1");
        driver.get(Config.get().uiBaseUrl() + "/overview.htm");
        AccountsOverviewPage overview = new AccountsOverviewPage(driver);
        WebElement row = overview.firstAccountRow();
        String before = row.getText();

        driver.navigate().refresh(); // reloading replaces the old rows with new elements
        assertThrows(StaleElementReferenceException.class, row::getText);
        assertEquals(overview.firstAccountRow().getText(), before, "the row text after the refresh");
    }
}
