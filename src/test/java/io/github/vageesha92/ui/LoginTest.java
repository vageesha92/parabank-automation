package io.github.vageesha92.ui;

import io.github.vageesha92.core.Config;
import io.github.vageesha92.core.DriverManager;
import io.github.vageesha92.ui.pages.AccountsOverviewPage;
import io.github.vageesha92.ui.pages.LoginPage;
import io.github.vageesha92.ui.pages.RegisterPage;
import org.openqa.selenium.WebDriver;
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
    public void wrongPasswordShowsError() {
        WebDriver driver = DriverManager.getDriver();

        LoginPage loginPage = new LoginPage(driver)
                .open(Config.get().uiBaseUrl());
        String error = loginPage.loginExpectingError("unknownUser123", "wrongPassword");

        assertEquals(error, "The username and password could not be verified.");

    }
}
