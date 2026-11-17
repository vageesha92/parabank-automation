package io.github.vageesha92.ui;

import io.github.vageesha92.core.DriverManager;
import io.github.vageesha92.db.TransactionExport;
import io.github.vageesha92.ui.pages.ExtrasPage;
import org.testng.annotations.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class StatementUploadTest extends BaseUiTest {

    // john's first account in the seed data (insert.sql), read only
    private static final long JOHNS_ACCOUNT = 12345;

    @Test
    public void uploadedStatementHasEveryTransactionRow() throws Exception {
        List<String> rows = TransactionExport.rowsOf(JOHNS_ACCOUNT);
        assertFalse(rows.isEmpty(), "no rows for account 12345, run the API suite once to reset ParaBank");
        Path csv = TransactionExport.writeCsv(JOHNS_ACCOUNT, rows);

        String shown = new ExtrasPage(DriverManager.getDriver()).open().uploadStatement(csv);

        assertEquals(shown, "Received " + csv.getFileName() + " (" + Files.size(csv) + " bytes, " + rows.size() + " rows).");
    }
}
