package io.github.vageesha92.db;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public final class TransactionExport {

    private TransactionExport() {
        // utility class
    }

    public static List<String> rowsOf(long accountId) throws SQLException {
        String sql = "SELECT description, amount FROM Transaction WHERE account_id = ? ORDER BY id DESC";
        List<String> rows = new ArrayList<>();
        try (Connection db = Db.connect();
             PreparedStatement query = db.prepareStatement(sql)) {
            query.setLong(1, accountId);
            try (ResultSet result = query.executeQuery()) {
                while (result.next()) {
                    rows.add(result.getString("description") + "," + result.getBigDecimal("amount").setScale(2));
                }
            }
        }
        return rows;
    }

    public static Path writeCsv(long accountId, List<String> rows) throws IOException {
        Path file = Path.of("target", "statement-" + accountId + ".csv");
        Files.createDirectories(file.getParent());
        List<String> lines = new ArrayList<>();
        lines.add("description,amount");
        lines.addAll(rows);
        return Files.write(file, lines);
    }
}
