package io.github.vageesha92.core;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ConfigTest {

    @Test
    public void localConfigHasAllValues() {
        Config config = Config.load();

        assertEquals(config.apiBaseUri().getPort(), 8080);
        assertEquals(config.gridUrl().getPort(), 4444);
        assertEquals(config.uiBaseUrl(), "http://parabank:8080/parabank",
                "ui.baseUrl should use the Compose service name, because the browser runs inside a container");
        assertEquals(config.browser(), Browser.CHROME);
        assertEquals(config.dbUrl(), "jdbc:hsqldb:hsql://localhost:19001/parabank");
    }
}
