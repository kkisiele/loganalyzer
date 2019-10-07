package loganalyzer;

import org.junit.After;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EndToEndTest {
    private static final String DATABASE_FILE_NAME = "endtoendtestdb";

    @After
    public void tearDown() throws Exception {
        deleteDatabaseFiles();
    }

    private void deleteDatabaseFiles() throws IOException {
        Files.delete(Paths.get(DATABASE_FILE_NAME + ".log"));
        Files.delete(Paths.get(DATABASE_FILE_NAME + ".properties"));
        Files.delete(Paths.get(DATABASE_FILE_NAME + ".script"));
        Files.delete(Paths.get(DATABASE_FILE_NAME + ".tmp"));
    }

    @Test
    public void processesProvidedLogFileByStoringEventsInDatabase() throws Exception {
        Main.main("samplelogfile.txt", DATABASE_FILE_NAME);
        List<Event> storedEvents = fetchAllEventsFromDatabase();
        assertEquals(3, storedEvents.size());
        assertEvent(storedEvents.get(0), "111", 5, true, "SECURITY_LOG", "12345");
        assertEvent(storedEvents.get(1), "333", 8, true, null, null);
        assertEvent(storedEvents.get(2), "222", 3, false, null, null);
    }

    private void assertEvent(Event event, String eventId, int duration, boolean alert, String type, String host) {
        assertEquals(eventId, event.getId());
        assertEquals(duration, event.getDurationInMilliseconds());
        assertEquals(alert, event.isAlert());
        assertEquals(type, event.getType());
        assertEquals(host, event.getHost());
    }

    private List<Event> fetchAllEventsFromDatabase() throws SQLException {
        List<Event> result = new LinkedList<>();
        try(Connection connection = DriverManager.getConnection(String.format("jdbc:hsqldb:file:%s", DATABASE_FILE_NAME), "SA", "")) {
            ResultSet rs = connection.createStatement().executeQuery("select * from events");
            while(rs.next()) {
                Event.Builder builder = new Event.Builder();
                builder.withId(rs.getString("event_id"));
                builder.withType(rs.getString("type"));
                builder.withHost(rs.getString("host"));
                builder.withDurationInMilliseconds(rs.getLong("duration"));
                builder.withAlert(rs.getBoolean("alert"));
                result.add(builder.build());
            }
        }
        return result;
    }
}
