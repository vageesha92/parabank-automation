package io.github.vageesha92.ui;

import io.github.vageesha92.core.Config;
import io.github.vageesha92.core.DriverFactory;
import io.github.vageesha92.core.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.Assert;

public abstract class BaseUiTest {

    protected static void logThread(String where) {
        System.out.println("[thread] " + where + " on " + Thread.currentThread().getName());
    }

    @BeforeSuite(alwaysRun = true)
    public void logSuiteStart() {
        logThread("BeforeSuite");
    }

    @BeforeMethod
    public void startBrowser() {
        String thread = Thread.currentThread().getName();
        logThread("BeforeMethod");
        Assert.assertFalse(
                DriverManager.hasDriver(),
                "Thread '" + thread + "' already holds a driver before BeforeMethod. "
                        + "A previous test on this thread did not release it.");
        DriverManager.setSession(DriverFactory.create(Config.get()));
    }

    @AfterMethod(alwaysRun = true)
    public void stopBrowser() {
        logThread("AfterMethod");
        DriverManager.quitDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void logSuiteEnd() {
        logThread("AfterSuite");
    }
}
