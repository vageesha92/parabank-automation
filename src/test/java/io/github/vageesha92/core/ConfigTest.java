package io.github.vageesha92.core;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ConfigTest {

    @Test
    public void localConfigHasFourValues() {
        Config config = Config.load();

        assertEquals(config.apiBaseUri().getPort(), 8080);
        assertEquals(config.gridUrl().getPort(), 4444);
        assertEquals(config.uiBaseUrl(), "http://parabank:8080/parabank",
                "ui.baseUrl must use the Compose service name: the browser runs in a container");
        assertEquals(config.browser(), Browser.CHROME,
                "browser must default to a node that `docker compose up -d` starts");
    }
}
