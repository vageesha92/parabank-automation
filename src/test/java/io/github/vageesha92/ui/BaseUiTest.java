package io.github.vageesha92.ui;

import io.github.vageesha92.core.Config;
import io.github.vageesha92.core.DriverFactory;
import io.github.vageesha92.core.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseUiTest {

    @BeforeMethod
    public void startBrowser() {
       DriverManager.setDriver(DriverFactory.create(Config.get()));
    }

    @AfterMethod(alwaysRun = true)
    public void stopBrowser() {
        DriverManager.quitDriver();
    }
}
