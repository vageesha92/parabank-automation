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

public class LoginPageSmokeTest extends BaseUiTest {

    @Test
    public void loginFormIsVisibleOnWelcomePage() {
        logThread("Test login form");

        WebDriver driver = DriverManager.getDriver();

        driver.get(Config.get().uiBaseUrl() + "/index.htm");

        WebElement username = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.name("username")));

        assertTrue(username.isDisplayed());
    }
}
