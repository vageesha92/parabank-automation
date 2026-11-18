package io.github.vageesha92.core;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;

public class FailureListener implements ITestListener {

    @Override
    public void onTestFailure(ITestResult result) {
        if (!DriverManager.hasDriver()) {
            return;
        }
        WebDriver driver = DriverManager.getDriver();

        byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

        Allure.addAttachment(
                "screenshot",
                "image/png",
                new ByteArrayInputStream(screenshot),
                "png"
        );
        Allure.addAttachment("last actions", String.join("\n", DriverManager.getListener().lastActions()));
        Allure.addAttachment("browser console", String.join("\n", DriverManager.getConsole().lines()));
    }
}
