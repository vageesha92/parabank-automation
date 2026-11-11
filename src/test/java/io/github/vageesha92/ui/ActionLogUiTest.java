package io.github.vageesha92.ui;

import io.github.vageesha92.core.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.*;

public class ActionLogUiTest extends BaseUiTest {

    @Test
    public void missingElementLeavesErrorEntry() {
        WebDriver driver = DriverManager.getDriver();

        assertThrows(NoSuchElementException.class, () -> driver.findElement(By.id("nope")));

        List<String> actions = DriverManager.getListener().lastActions();

        assertTrue(actions.stream().anyMatch(line -> line.contains("ERROR NoSuchElementException")),
                "no ERROR entry in: " + actions);
    }

    @Test
    public void bufferHoldsOnlyThisTestsCalls() {
        WebDriver driver = DriverManager.getDriver();
        driver.getTitle();
        driver.getTitle();
        driver.getTitle();

        List<String> actions = DriverManager.getListener().lastActions();

        assertEquals(actions.size(), 3,
                "expected exactly 3 actions, but got: " + actions);
    }
}
