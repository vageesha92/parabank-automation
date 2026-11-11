package io.github.vageesha92.core;

import org.openqa.selenium.WebDriver;

public final class DriverManager {

    private static final ThreadLocal<DriverSession> SESSION = new ThreadLocal<>();

    private DriverManager() {
        // utility class
    }

    public static boolean hasDriver() {
        return SESSION.get() != null;
    }

    public static void setSession(DriverSession session) {
        SESSION.set(session);
    }

    public static DriverSession current() {
        DriverSession session = SESSION.get();
        if (session == null) {
            throw new IllegalStateException("No WebDriver set for this thread: " + Thread.currentThread().getName());
        }
        return session;
    }

    public static WebDriver getDriver() {
        return current().driver();
    }

    public static ActionListener getListener() {
        return current().listener();
    }

    public static void quitDriver() {
        DriverSession session = SESSION.get();
        try {
            if (session != null) {
                session.driver().quit();
            }
        } finally {
            SESSION.remove();
        }
    }
}
