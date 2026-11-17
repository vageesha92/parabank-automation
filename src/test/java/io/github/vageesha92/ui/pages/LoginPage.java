package io.github.vageesha92.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private static final By USERNAME = By.name("username");
    private static final By PASSWORD = By.name("password");
    private static final By LOG_IN = By.cssSelector("#loginPanel input[type='submit']");
    private static final By ERROR = By.cssSelector("#rightPanel p.error");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open(String baseUrl) {
        driver.get(baseUrl + "/index.htm");
        visible(USERNAME);
        return this;
    }

    public AccountsOverviewPage loginAs(String username, String password) {
        submit(username, password);
        return new AccountsOverviewPage(driver);
    }

    public AccountsOverviewPage loginWithEnter(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password + Keys.ENTER);
        return new AccountsOverviewPage(driver);
    }

    // reuses a session that was logged in over HTTP, so no login form
    public AccountsOverviewPage useSession(String baseUrl, String sessionId) {
        driver.get(baseUrl + "/index.htm"); // a cookie can only be set for the current page's domain
        // swap in the logged-in JSESSIONID (path /parabank, no domain)
        driver.manage().deleteCookieNamed("JSESSIONID");

        Cookie sessionCookie = new Cookie.Builder("JSESSIONID", sessionId)
                .path("/parabank")
                .isHttpOnly(true)
                .build();

        driver.manage().addCookie(sessionCookie);
        driver.get(baseUrl + "/overview.htm");
        return new AccountsOverviewPage(driver);
    }

    public String loginExpectingError(String username, String password) {
        submit(username, password);
        return text(ERROR);
    }

    private void submit(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(LOG_IN);
    }
}
