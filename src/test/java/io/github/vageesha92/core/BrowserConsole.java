package io.github.vageesha92.core;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.module.LogInspector;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class BrowserConsole implements AutoCloseable {

    private final LogInspector inspector;
    private final List<String> lines = new CopyOnWriteArrayList<>();

    public BrowserConsole(WebDriver driver) {
        this.inspector = new LogInspector(driver);
        // runs on a Selenium thread, not the test thread - don't call the driver from here
        inspector.onConsoleEntry(entry -> lines.add(entry.getLevel() + ": " + entry.getText()));
    }

    public List<String> lines() {
        return List.copyOf(lines);
    }

    public boolean has(String start) {
        return lines.stream().anyMatch(line -> line.startsWith(start));
    }

    @Override
    public void close() {
        inspector.close();
    }
}
