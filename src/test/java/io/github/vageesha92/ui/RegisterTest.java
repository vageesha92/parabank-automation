package io.github.vageesha92.ui;

import io.github.vageesha92.core.Config;
import io.github.vageesha92.core.DriverManager;
import io.github.vageesha92.ui.pages.RegisterPage;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class RegisterTest extends BaseUiTest {

    // One row for each required field: the form path and ParaBank's error text (messages.properties:239-246).
    @DataProvider(name = "missingFields")
    public Object[][] missingFields() {
        return new Object[][] {
                {"customer.firstName", "First name is required."},
                {"customer.address.zipCode", "Zip Code is required."},
                {"customer.username", "Username is required."},
                {"customer.ssn", "Social Security Number is required."},
        };
    }

    @Test(dataProvider = "missingFields")
    public void emptyRequiredFieldShowsItsError(String path, String expectedError) {
        RegisterPage page = new RegisterPage(DriverManager.getDriver())
                .open(Config.get().uiBaseUrl())
                .submitWithEmpty(path);

        String actualError = page.errorFor(path);
        assertEquals(actualError, expectedError);
    }
}
