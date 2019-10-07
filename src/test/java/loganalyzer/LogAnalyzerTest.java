package loganalyzer;

import loganalyzer.Log.Builder;
import loganalyzer.Log.State;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LogAnalyzerTest {
    private final LogRepository logRepository = new InMemoryLogRepository(
            new Builder().withId("1").withState(State.STARTED).build(),
            new Builder().withId("2").withState(State.STARTED).build(),
            new Builder().withId("1").withState(State.FINISHED).build()
    );
    private final InMemoryEventRepository eventRepository = new InMemoryEventRepository();
    private final LogAnalyzer analyzer = new LogAnalyzer(logRepository, eventRepository, new OpsSupport());

    @Test
    public void savesEventWhenRelatedLogsAreProvided() {
        analyzer.run();
        assertEquals(1, eventRepository.events().size());
    }
}