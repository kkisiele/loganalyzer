package loganalyzer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

final class BatchedEventRepository implements EventRepository {
    private static final int DEFAULT_BATCH_SIZE = 25;

    private final EventRepository eventRepository;
    private final List<Event> pendingEvents = new LinkedList<>();
    private final int batchSize;

    public BatchedEventRepository(EventRepository eventRepository) {
        this(eventRepository, DEFAULT_BATCH_SIZE);
    }

    public BatchedEventRepository(EventRepository eventRepository, int batchSize) {
        this.eventRepository = eventRepository;
        this.batchSize = batchSize;
    }

    @Override
    public void save(List<Event> events) {
        pendingEvents.addAll(events);
        if(pendingEvents.size() >= batchSize) {
            flush();
        }
    }

    public void save(Event event) {
        save(Arrays.asList(event));
    }

    public void flush() {
        eventRepository.save(pendingEvents);
        pendingEvents.clear();
    }
}
