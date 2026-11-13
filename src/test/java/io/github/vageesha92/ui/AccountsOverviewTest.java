package io.github.vageesha92.ui;

import io.github.vageesha92.core.Config;
import io.github.vageesha92.core.DriverManager;
import io.github.vageesha92.ui.pages.AccountsOverviewPage;
import io.github.vageesha92.ui.pages.AccountsOverviewPage.AccountRow;
import io.github.vageesha92.ui.pages.RegisterPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

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
    }
}
