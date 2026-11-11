package io.github.vageesha92.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountsOverviewPage extends BasePage {

    private static final By TITLE = By.cssSelector("#showOverview h1.title");

    public AccountsOverviewPage(WebDriver driver) {
        super(driver);
    }

    public String title() {
        return text(TITLE);
    }
}
