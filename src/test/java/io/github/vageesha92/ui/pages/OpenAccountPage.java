package io.github.vageesha92.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class OpenAccountPage extends BasePage {

    private static final By TYPE = By.id("type");
    private static final By FROM_ACCOUNT_OPTIONS = By.cssSelector("#fromAccountId option");
    private static final By OPEN = By.cssSelector("#openAccountForm input[type='button']");
    private static final By NEW_ACCOUNT_ID = By.id("newAccountId");

    public OpenAccountPage(WebDriver driver) {
        super(driver);
    }

    public OpenAccountPage open(String baseUrl) {
        driver.get(baseUrl + "/openaccount.htm");
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(FROM_ACCOUNT_OPTIONS, 0));
        return this;
    }

    public String openAccount(String type) {
        new Select(visible(TYPE)).selectByVisibleText(type);
        click(OPEN);
        return text(NEW_ACCOUNT_ID); // waits until the AJAX result is visible
    }
}
