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

    @Test
    public void confirmedTransferShowsResultAlert() {
        ExtrasPage extras = new ExtrasPage(DriverManager.getDriver()).open();
        assertEquals(extras.startTransfer(), "Confirm this transfer?");

        assertEquals(extras.answerConfirm(true), "Transfer complete: $100.00 to account 54321.");
        assertEquals(extras.transferResult(), "Transfer complete: $100.00 to account 54321.");
    }

    @Test
    public void cancelledTransferSaysSo() {
        ExtrasPage extras = new ExtrasPage(DriverManager.getDriver()).open();
        extras.startTransfer();
        assertEquals(extras.answerConfirm(false), "Transfer cancelled.");
        assertEquals(extras.transferResult(), "Transfer cancelled.");
    }

    @Test
    public void helpChatAnswersInsideItsFrame() {
        ExtrasPage extras = new ExtrasPage(DriverManager.getDriver()).open();
        assertFalse(extras.chatBoxInMainPage(), "the chat box is inside the iframe, not in the main page");

        assertEquals(extras.askHelpChat("Why was my login refused?"), "Agent: Thanks, an agent will answer soon.");
        assertEquals(extras.heading(), "ParaBank extras (practice page)", "back on the main page");
    }
}
