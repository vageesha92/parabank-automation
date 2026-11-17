package io.github.vageesha92.db;

import io.github.vageesha92.core.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Db {

    private Db() {
        // utility class
    }

    public static Connection connect() throws SQLException {
        // java.sql.DriverManager (JDBC), not our core.DriverManager
        return DriverManager.getConnection(Config.get().dbUrl(), "sa", "");
    }
}
