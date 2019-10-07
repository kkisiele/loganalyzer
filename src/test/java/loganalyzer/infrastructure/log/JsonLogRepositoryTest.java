package loganalyzer.infrastructure.log;

import loganalyzer.Log;
import loganalyzer.OpsSupport;
import loganalyzer.Log.State;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class JsonLogRepositoryTest {
    private final JsonLogRepository repository = new JsonLogRepository(getClass().getResourceAsStream("/logfile.txt"), new OpsSupport());

    @Test
    public void deserializesAllLogsFromProvidedFile() {
        List<Log> logs = repository.findAll();
        assertEquals(6, logs.size());
        assertLog(logs.get(0), "111", State.STARTED, "SECURITY_LOG", "12345", 1491378595212L);
        assertLog(logs.get(1), "222", State.STARTED, null, null, 1491378595213L);
        assertLog(logs.get(2), "333", State.FINISHED, null, null, 1491378595218L);
    }

    private void assertLog(Log log, String eventId, State state, String type, String host, long timestamp) {
        assertEquals(eventId, log.getEventId());
        assertEquals(state, log.getState());
        assertEquals(type, log.getType());
        assertEquals(host, log.getHost());
        assertEquals(timestamp, log.getTimestamp());
    }
}