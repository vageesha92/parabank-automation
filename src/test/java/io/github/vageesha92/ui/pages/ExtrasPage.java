package io.github.vageesha92.ui.pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class ExtrasPage extends BasePage {

    public static final String URL = "http://extras/";

    private static final By HEADING = By.cssSelector("h1.title");
    private static final By MENU = By.id("accountMenu");
    private static final By MENU_TRIGGER = By.id("accountMenuTrigger");
    private static final By MENU_RESULT = By.id("menuResult");
    private static final By TRANSFER = By.id("transferButton");
    private static final By TRANSFER_RESULT = By.id("transferResult");
    private static final By HELP_CHAT = By.id("helpChat");
    private static final By CHAT_INPUT = By.id("chatInput");
    private static final By CHAT_SEND = By.id("chatSend");
    private static final By CHAT_LAST_LINE = By.cssSelector("#chatLog li:last-child");

    public ExtrasPage(WebDriver driver) {
        super(driver);
    }

    public ExtrasPage open() {
        driver.get(URL);
        visible(HEADING);
        return this;
    }

    // menu items are only shown while the mouse is over the menu
    public boolean isMenuItemShown(String itemId) {
        return driver.findElement(By.id(itemId)).isDisplayed();
    }

    public String chooseFromAccountMenu(String itemId) {
        scrollToCentre(MENU); // Firefox only moves the mouse inside the window, and the menu is below the fold
        WebElement item = driver.findElement(By.id(itemId));
        new Actions(driver)
                .moveToElement(driver.findElement(MENU_TRIGGER))
                .perform();

        wait.until(ExpectedConditions.visibilityOf(item));

        new Actions(driver)
                .moveToElement(item)
                .click()
                .perform();

        return text(MENU_RESULT);
    }

    // A browser dialog is not part of the page. switchTo().alert() is needed to reach it.
    public String startTransfer() {
        click(TRANSFER);
        return wait.until(ExpectedConditions.alertIsPresent()).getText();
    }

    public String answerConfirm(boolean ok) {
        Alert confirm = driver.switchTo().alert();
        if (ok) {
            confirm.accept();
        } else {
            confirm.dismiss();
        }
        Alert result = wait.until(ExpectedConditions.alertIsPresent());
        String text = result.getText();
        result.accept();
        return text;
    }

    public String transferResult() {
        return text(TRANSFER_RESULT);
    }

    public String heading() {
        return text(HEADING);
    }

    // the chat is a separate page in an <iframe>; the driver sees one page at a time
    public boolean chatBoxInMainPage() {
        return !driver.findElements(CHAT_INPUT).isEmpty();
    }

    // switches back to the main page before returning
    public String askHelpChat(String question) {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(HELP_CHAT)); // switchTo().frame(...)
        try {
            type(CHAT_INPUT, question);
            click(CHAT_SEND);
            return text(CHAT_LAST_LINE);
        } finally {
            driver.switchTo().defaultContent();
        }
    }
}
