package loganalyzer.infrastructure.storage;

import loganalyzer.Event;
import loganalyzer.EventRepository;
import loganalyzer.OpsSupport;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

public final class JdbcEventRepository implements EventRepository {
    private static final String INSERT_EVENT_SQL = "INSERT INTO events(event_id, duration, type, host, alert) values(?, ?, ?, ?, ?)";

    private final ConnectionFactory connectionFactory;
    private final OpsSupport opsSupport;

    public JdbcEventRepository(Database database, OpsSupport opsSupport) {
        this.connectionFactory = new ConnectionFactory(() -> database.createConnection());
        this.opsSupport = opsSupport;
    }

    @Override
    public void save(List<Event> events) {
        if(events.size() == 0) {
            return;
        }

        try {
            save(connectionFactory.get(), events);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void save(Connection connection, List<Event> events) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(INSERT_EVENT_SQL)) {
            addBatch(statement, events);
            statement.executeBatch();
            connection.commit();
        }
    }

    private void addBatch(PreparedStatement statement, List<Event> events) throws SQLException {
        for(Event event : events) {
            addBatch(statement, event);
        }
    }

    private void addBatch(PreparedStatement statement, Event event) throws SQLException {
        opsSupport.savingEvent(event);
        statement.setString(1, event.getId());
        statement.setLong(2, event.getDurationInMilliseconds());
        statement.setString(3, event.getType());
        statement.setString(4, event.getHost());
        statement.setBoolean(5, event.isAlert());
        statement.addBatch();
    }

    private static class ConnectionFactory {
        private final Supplier<Connection> supplier;
        private Connection connection;

        public ConnectionFactory(Supplier<Connection> supplier) {
            this.supplier = Objects.requireNonNull(supplier);
        }

        public Connection get() {
            if(connection == null) {
                connection = supplier.get();
            }
            return connection;
        }
    }
}
