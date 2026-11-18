package io.github.vageesha92.ui;

import io.github.vageesha92.core.Config;
import io.github.vageesha92.core.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;

import static org.testng.Assert.assertEquals;

public class WindowsAndTabsTest extends BaseUiTest {

    // ParaBank's footer link to parasoft.com opens in a new window (footerPanel.jsp:17, target="_blank").
    private static final By PARASOFT_LINK = By.cssSelector("#footerPanel ul.visit a[target='_blank']");

    @Test
    public void footerLinkOpensSecondWindow() {
        WebDriver driver = DriverManager.getDriver();
        driver.get(Config.get().uiBaseUrl() + "/index.htm");
        String home = driver.getWindowHandle();

        driver.findElement(PARASOFT_LINK).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.numberOfWindowsToBe(2));
        assertEquals(driver.getWindowHandle(), home);

        String other = driver.getWindowHandles().stream().filter(h -> !h.equals(home)).findFirst().orElseThrow();
        driver.switchTo().window(other);
        driver.close();                 // closes the current window, which is the new one
        driver.switchTo().window(home); // after close() the driver has no window until we switch back

        assertEquals(driver.getWindowHandles().size(), 1);
        assertEquals(driver.getTitle(), "ParaBank | Welcome | Online Banking");
    }

    @Test
    public void newTabCanOpenAnotherPage() {
        WebDriver driver = DriverManager.getDriver();
        driver.get(Config.get().uiBaseUrl() + "/index.htm");
        String home = driver.getWindowHandle();

        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(Config.get().uiBaseUrl() + "/about.htm");
        assertEquals(driver.getTitle(), "ParaBank | About Us");

        driver.close();
        driver.switchTo().window(home);
        assertEquals(driver.getWindowHandles().size(), 1);
    }
}
