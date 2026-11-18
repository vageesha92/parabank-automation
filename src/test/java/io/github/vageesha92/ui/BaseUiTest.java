package io.github.vageesha92.ui;

import io.github.vageesha92.core.Config;
import io.github.vageesha92.core.DriverFactory;
import io.github.vageesha92.core.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.Assert;

public abstract class BaseUiTest {

    protected static void logThread(String where) {
        System.out.println("[thread] " + where + " on " + Thread.currentThread().getName());
    }

    @BeforeSuite(alwaysRun = true)
    public void logSuiteStart() {
        logThread("BeforeSuite");
    }

    // browser comes from testng-ui.xml; with -Dtest there is no parameter, so Config decides
    @BeforeMethod
    @Parameters("browser")
    public void startBrowser(@Optional("") String browser) {
        String thread = Thread.currentThread().getName();
        Config config = browser.isBlank() ? Config.get() : Config.get().withBrowser(browser);
        logThread("BeforeMethod " + config.browser());
        Assert.assertFalse(
                DriverManager.hasDriver(),
                "Thread '" + thread + "' already holds a driver before BeforeMethod. "
                        + "A previous test on this thread did not release it.");
        DriverManager.setSession(DriverFactory.create(config));
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
