package io.github.vageesha92.ui;

import io.github.vageesha92.core.Config;
import io.github.vageesha92.core.DriverManager;
import io.github.vageesha92.ui.pages.AccountsOverviewPage;
import io.github.vageesha92.ui.pages.LoginPage;
import io.github.vageesha92.ui.pages.RegisterPage;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class LoginTest extends BaseUiTest {

    @Test
    public void newCustomerCanLogIn() {
        WebDriver driver = DriverManager.getDriver();
        String user = RegisterPage.uniqueUsername();
        new RegisterPage(driver).open(Config.get().uiBaseUrl()).register(user, "secret1");
        driver.manage().deleteAllCookies(); // log out by forgetting the session

        AccountsOverviewPage overview = new LoginPage(driver)
                .open(Config.get().uiBaseUrl())
                .loginAs(user, "secret1");

        assertEquals(overview.title(), "Accounts Overview");
    }

    @Test
    public void loginWithEnterKey() {
        WebDriver driver = DriverManager.getDriver();
        String user = RegisterPage.uniqueUsername();
        new RegisterPage(driver).open(Config.get().uiBaseUrl()).register(user, "secret1");
        driver.manage().deleteAllCookies(); // log out by forgetting the session

        AccountsOverviewPage overview = new LoginPage(driver)
                .open(Config.get().uiBaseUrl())
                .loginWithEnter(user, "secret1");

        assertEquals(overview.title(), "Accounts Overview");
    }

    // One row for each bad login: username, password and ParaBank's error text (messages.properties:189-190).
    @DataProvider(name = "badLogins")
    public Object[][] badLogins() {
        return new Object[][] {
                // Empty username
                {"", "secret1", "Please enter a username and password."},

                // Empty password
                {"validuser", "", "Please enter a username and password."},

                // Wrong password
                {"validuser", "wrongpassword", "The username and password could not be verified."}
        };
    }

    @Test(dataProvider = "badLogins")
    public void badLoginShowsError(String username, String password, String expectedError) {
        String actualError = new LoginPage(DriverManager.getDriver())
                .open(Config.get().uiBaseUrl())
                .loginExpectingError(username, password);

        assertEquals(actualError, expectedError);
    }
}
