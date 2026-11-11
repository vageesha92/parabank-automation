package io.github.vageesha92.core;

import org.openqa.selenium.WebDriver;

public record DriverSession(WebDriver driver, ActionListener listener) {
}
