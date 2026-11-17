package io.github.vageesha92.core;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ThreadGuard;
import org.openqa.selenium.support.events.EventFiringDecorator;

import java.net.MalformedURLException;

public class DriverFactory {

    public static DriverSession create(Config config) {
        Capabilities options = switch (config.browser()) {
            case CHROME -> new ChromeOptions();
            case FIREFOX -> new FirefoxOptions();
            case EDGE -> new EdgeOptions();
        };
        WebDriver driver = null;
        try {
            RemoteWebDriver remote = new RemoteWebDriver(config.gridUrl().toURL(), options);
            // Lets sendKeys(path) on a file input upload a file from this machine to the node.
            // It must be set here, before the driver is augmented and wrapped.
            remote.setFileDetector(new LocalFileDetector());
            driver = remote;

            driver.manage().window().setSize(new Dimension(1366, 768));

            WebDriver augmented = new Augmenter().augment(driver);
            ActionListener listener = new ActionListener();
            WebDriver decorated = new EventFiringDecorator<>(listener).decorate(augmented);

            // ThreadGuard lets only the thread that created the driver use it.
            // A call from another thread fails with a WebDriverException instead of causing a race.
            return new DriverSession(ThreadGuard.protect(decorated), listener);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Bad grid.url: " + config.gridUrl(), e);
        } catch (RuntimeException e) {
            // session exists but setup failed: quit it, otherwise the Grid slot stays busy
            if (driver != null) {
                try {
                    driver.quit();
                } catch (RuntimeException quitFailure) {
                    e.addSuppressed(quitFailure);
                }
            }
            throw e;
        }
    }
}
