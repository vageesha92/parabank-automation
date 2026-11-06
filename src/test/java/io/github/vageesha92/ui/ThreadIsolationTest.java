package io.github.vageesha92.ui;

import io.github.vageesha92.core.Config;
import io.github.vageesha92.core.DriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class ThreadIsolationTest extends BaseUiTest {

    @Test
    public void anotherThreadCannotUseThisDriver() throws InterruptedException {
        logThread("Test thread isolation");

        WebDriver driver = DriverManager.getDriver();
        driver.get(Config.get().uiBaseUrl() + "/index.htm");

        AtomicReference<Throwable> onTheObject = new AtomicReference<>();
        AtomicReference<Throwable> fromTheManager = new AtomicReference<>();

        Thread intruder = new Thread(() -> {
            try {
                driver.getTitle();
            } catch (Throwable t) {
                onTheObject.set(t);
            }
            try {
                DriverManager.getDriver();
            } catch (Throwable t) {
                fromTheManager.set(t);
            }
        }, "intruder");

        intruder.start();
        intruder.join();

        Throwable onObject = onTheObject.get();
        assertNotNull(onObject, "another thread was able to use this test's driver");
        assertTrue(onObject instanceof WebDriverException,
                "expected a WebDriverException from ThreadGuard, but got: " + onObject);
        assertTrue(onObject.getMessage().contains("Thread safety error"),
                "expected a thread safety error message, but got: " + onObject.getMessage());

        Throwable fromManager = fromTheManager.get();
        assertNotNull(fromManager, "DriverManager returned a driver to a thread that never set one");
        assertTrue(fromManager instanceof IllegalStateException,
                "expected IllegalStateException from DriverManager, but got: " + fromManager);
    }
}
