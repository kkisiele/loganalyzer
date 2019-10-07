package loganalyzer.infrastructure.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public final class Database implements AutoCloseable {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final String connectionUrl;
    private final String user;
    private final String password;

    private final List<Connection> activeConnections = new LinkedList<>();

    public static Database setup(String connectionUrl, String user, String password) throws SQLException {
        Database database = new Database(connectionUrl, user, password);
        database.setup();
        return database;
    }

    private Database(String connectionUrl, String user, String password) {
        this.connectionUrl = connectionUrl;
        this.user = user;
        this.password = password;
    }

    private void setup() throws SQLException {
        try(Connection connection = makeConnection()) {
            DatabaseSchema.create(connection);
        }
    }

    public Connection createConnection() {
        Connection connection = makeConnection();
        activeConnections.add(connection);
        return connection;
    }

    private Connection makeConnection() {
        try {
            Connection connection = DriverManager.getConnection(connectionUrl, user, password);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException ex) {
            throw new RuntimeException("Error getting connection: " + connectionUrl);
        }
    }

    @Override
    public void close() throws Exception {
        closeActiveConnections();
    }

    private void closeActiveConnections() {
        activeConnections.forEach(connection -> close(connection));
        activeConnections.clear();
    }

    private void close(Connection connection) {
        if(connection != null) {
            try {
                connection.close();
            } catch(SQLException ex) {
                log.warn("Error closing connection", ex);
            }
        }
    }
}
