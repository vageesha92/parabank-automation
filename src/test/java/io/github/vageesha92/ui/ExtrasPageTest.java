package io.github.vageesha92.ui;

import io.github.vageesha92.core.DriverManager;
import io.github.vageesha92.ui.pages.ExtrasPage;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class ExtrasPageTest extends BaseUiTest {

    @Test
    public void accountMenuOpensOnHover() {
        ExtrasPage extras = new ExtrasPage(DriverManager.getDriver()).open();
        assertFalse(extras.isMenuItemShown("menuStatements"), "the menu is closed before the hover");

        assertEquals(extras.chooseFromAccountMenu("menuStatements"), "You chose: Statements.");
    }
}
