package loganalyzer;

import org.junit.Assert;
import org.junit.Test;

public class BatchedEventRepositoryTest {
    private final InMemoryEventRepository eventRepository = new InMemoryEventRepository();
    private final BatchedEventRepository batchedEventRepository = new BatchedEventRepository(eventRepository, 3);

    @Test
    public void savesInEventPersistenceWhenBatchSizeReached() {
        batchedEventRepository.save(new Event.Builder().withId("1").build());
        Assert.assertEquals(0, eventRepository.events().size());
        batchedEventRepository.save(new Event.Builder().withId("2").build());
        Assert.assertEquals(0, eventRepository.events().size());

        batchedEventRepository.save(new Event.Builder().withId("3").build());
        Assert.assertEquals(3, eventRepository.events().size());
    }
}