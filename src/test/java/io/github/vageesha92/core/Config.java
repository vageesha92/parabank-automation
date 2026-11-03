package io.github.vageesha92.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.Locale;
import java.util.Properties;

public record Config(
        URI apiBaseUri,
        String uiBaseUrl,
        URI gridUrl,
        Browser browser
) {

    private static final Config INSTANCE = load();

    public static Config get() {
        return INSTANCE;
    }

    // Not cached, so ConfigTest can call it directly.
    static Config load() {
        String env = System.getProperty("env", "local");
        String resource = "/config/" + env + ".properties";
        Properties file = new Properties();
        try (InputStream in = Config.class.getResourceAsStream(resource)) {
            if (in == null) {
                throw new IllegalStateException("No config file on the classpath: " + resource);
            }
            file.load(in);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot read " + resource, e);
        }
        return new Config(
                URI.create(read(file, "api.baseUri")),
                read(file, "ui.baseUrl"),
                URI.create(read(file, "grid.url")),
                Browser.valueOf(read(file, "browser").toUpperCase(Locale.ROOT)));
    }

    // A -D value on the command line wins over the file.
    private static String read(Properties file, String key) {
        String value = System.getProperty(key, file.getProperty(key));
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("Missing config key: " + key);
        }
        return value.trim();
    }
}
