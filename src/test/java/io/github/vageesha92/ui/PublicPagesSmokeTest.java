package io.github.vageesha92.ui;

import io.github.vageesha92.core.Config;
import io.github.vageesha92.core.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.assertTrue;

public class PublicPagesSmokeTest extends BaseUiTest {

    @Test
    public void headerPanelIsOnWelcomePage() {
        assertVisible("/index.htm", By.cssSelector("#headerPanel"));
    }

    @Test
    public void firstNameFieldIsOnRegisterPage() {
        assertVisible("/register.htm", By.id("customer.firstName"));
    }

    @Test
    public void lastNameFieldIsOnRegisterPage() {
        assertVisible("/register.htm", By.id("customer.lastName"));
    }

    private void assertVisible(String path, By locator) {
        logThread("Test " + path);

        WebDriver driver = DriverManager.getDriver();

        driver.get(Config.get().uiBaseUrl() + path);

        WebElement element = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));

        assertTrue(element.isDisplayed(), "must be visible: " + locator + " on " + path);
    }
}
