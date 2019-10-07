package loganalyzer.infrastructure.log;

import loganalyzer.Log;
import loganalyzer.OpsSupport;
import loganalyzer.Log.State;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonLogAdapterTest {
    private JsonLogAdapter adapter = new JsonLogAdapter(new OpsSupport());

    @Test
    public void returnsDeserializedLogForValidJsonString() {
        Log log = adapter.toLog("{\"id\":\"scsmbstgra\", \"state\":\"STARTED\", \"type\":\"APPLICATION_LOG\", \"host\":\"12345\", \"timestamp\":1491377495212}");
        assertEquals("scsmbstgra", log.getEventId());
        assertEquals(State.STARTED, log.getState());
        assertEquals("APPLICATION_LOG", log.getType());
        assertEquals("12345", log.getHost());
        assertEquals(1491377495212L, log.getTimestamp());
    }

    @Test
    public void returnsNullForInvalidJsonString() {
        Log event = adapter.toLog("some invalid format");
        assertNull(event);
    }
}