package io.github.vageesha92.ui.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class TransferFundsPage extends BasePage {

    private static final By AMOUNT = By.id("amount");
    private static final By FROM = By.id("fromAccountId");
    private static final By TO = By.id("toAccountId");
    private static final By TO_ACCOUNT_OPTIONS = By.cssSelector("#toAccountId option");
    private static final By TRANSFER = By.cssSelector("#transferForm input[type='submit']");
    private static final By RESULT_TITLE = By.cssSelector("#showResult h1");

    public TransferFundsPage(WebDriver driver) {
        super(driver);
    }

    public TransferFundsPage open(String baseUrl) {
        driver.get(baseUrl + "/transfer.htm");
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(TO_ACCOUNT_OPTIONS, 0));
        return this;
    }

    public String transfer(String amount, String fromAccountId, String toAccountId) {
        type(AMOUNT, amount);
        new Select(visible(FROM)).selectByValue(fromAccountId);
        new Select(visible(TO)).selectByValue(toAccountId);

        wait.until(ExpectedConditions.elementToBeClickable(TRANSFER)).click();

        return text(RESULT_TITLE); // waits until the title is visible, then reads it
    }
}
