package io.github.vageesha92.core;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;

public class DriverFactory {

    public static WebDriver create(Config config) {
        Capabilities options = switch (config.browser()) {
            case CHROME -> new ChromeOptions();
            case FIREFOX -> new FirefoxOptions();
            case EDGE -> new EdgeOptions();
        };
        try {
            return new RemoteWebDriver(config.gridUrl().toURL(), options);
        } catch (MalformedURLException e) {
            throw new IllegalStateException("Bad grid.url: " + config.gridUrl(), e);
        }
    }
}
