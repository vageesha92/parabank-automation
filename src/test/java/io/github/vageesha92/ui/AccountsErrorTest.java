package io.github.vageesha92.ui;

import io.github.vageesha92.core.BrowserConsole;
import io.github.vageesha92.core.Config;
import io.github.vageesha92.core.DriverManager;
import io.github.vageesha92.ui.pages.AccountsOverviewPage;
import io.github.vageesha92.ui.pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.bidi.module.Network;
import org.openqa.selenium.bidi.network.AddInterceptParameters;
import org.openqa.selenium.bidi.network.BytesValue;
import org.openqa.selenium.bidi.network.Header;
import org.openqa.selenium.bidi.network.InterceptPhase;
import org.openqa.selenium.bidi.network.ProvideResponseParameters;
import org.openqa.selenium.bidi.network.UrlPattern;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class AccountsErrorTest extends BaseUiTest {

    // john = customer 12212 in the seed data; the fake 500 only reaches this test's browser
    private static final String JOHNS_ACCOUNTS = "/parabank/services_proxy/bank/customers/12212/accounts";

    @Test
    public void serverErrorOnAccountsCallShowsErrorBlock() {
        WebDriver driver = DriverManager.getDriver();
        AccountsOverviewPage overview = new LoginPage(driver).open(Config.get().uiBaseUrl()).loginAs("john", "demo");
        BrowserConsole console = DriverManager.getConsole();

        try (Network network = new Network(driver)) {
            network.addIntercept(new AddInterceptParameters(InterceptPhase.BEFORE_REQUEST_SENT)
                    .urlPattern(new UrlPattern().pathname(JOHNS_ACCOUNTS)));
            network.onBeforeRequestSent(event -> {
                if (event.isBlocked()) { // only requests paused by our intercept (runs on the BiDi thread)
                    network.provideResponse(new ProvideResponseParameters(event.getRequest().getRequestId())
                            .statusCode(500).reasonPhrase("Internal Server Error")
                            .headers(List.of(new Header("Content-Type",
                                    new BytesValue(BytesValue.Type.STRING, "application/json"))))
                            .body(new BytesValue(BytesValue.Type.STRING, "{\"message\":\"fake 500\"}")));
                }
            });
            driver.navigate().refresh();
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(d -> console.has("error: Server returned"));

            assertEquals(
                    overview.errorText(),
                    "An internal error has occurred and has been logged.",
                    "ParaBank's server error text"
            );

            assertTrue(
                    console.has("error: Server returned error"),
                    "no server error in the console:\n"
                            + console.lines()
            );

        }
    }
}
