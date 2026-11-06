package io.github.vageesha92.ui;

import io.github.vageesha92.core.Config;
import io.github.vageesha92.core.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.assertTrue;

public class PublicPagesDataProviderTest extends BaseUiTest {

    @DataProvider(name = "publicPages", parallel = true)
    public Object[][] publicPages() {
        return new Object[][] {
                {"/index.htm"},
                {"/about.htm"},
                {"/services.htm"},
                {"/contact.htm"},
                {"/sitemap.htm"},
                {"/register.htm"},
        };
    }

    @Test(dataProvider = "publicPages")
    public void headerPanelIsOnEachPublicPage(String path) {
        logThread("Row " + path);

        WebDriver driver = DriverManager.getDriver();

        driver.get(Config.get().uiBaseUrl() + path);

        WebElement header = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#headerPanel")));

        assertTrue(header.isDisplayed(), "header panel must be visible on " + path);
    }
}
