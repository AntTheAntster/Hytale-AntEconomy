package uk.anttheantster.anteconomy.utils;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {

    private final EconomyConfig config;
    private Connection connection;

    public MySQL(EconomyConfig config) {
        this.config = config;
    }

    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public void connect() throws SQLException {
        if (!config.isDbEnabled()) return;

        if (!isConnected()) {
            String url = "jdbc:mysql://" + config.getDbHost() + ":" + config.getDbPort() + "/" + config.getDbName()
                    + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

            try {
                DriverManager.registerDriver(new Driver());
            } catch (SQLException e) {
                throw new SQLException("Failed to register MySQL JDBC driver", e);
            }

            connection = DriverManager.getConnection(
                    url,
                    config.getDbUser(),
                    config.getDbPassword()
            );
        }
    }

    public void disconnect() {
        if (connection != null) {
            try { connection.close(); } catch (SQLException ignored) {}
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
