package loganalyzer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

final class DefaultLogHandle implements LogHandle {
    private final BatchedEventRepository eventRepository;
    private final FlagEventStrategy flagEventStrategy;
    private final Map<String, Log> unhandledLogs = new HashMap<>();

    public DefaultLogHandle(EventRepository eventRepository, FlagEventStrategy flagEventStrategy) {
        this.eventRepository = new BatchedEventRepository(eventRepository);
        this.flagEventStrategy = flagEventStrategy;
    }

    @Override
    public void handle(Log log) {
        if(hasPreviousLog(log)) {
            makeEvent(log);
        } else {
            addForLaterHandling(log);
        }
    }

    private boolean hasPreviousLog(Log log) {
        return unhandledLogs.containsKey(log.getEventId());
    }

    private void makeEvent(Log log) {
        Log existingLog = unhandledLogs.remove(log.getEventId());
        Event event = EventAdapter.toEvent(Arrays.asList(log, existingLog), flagEventStrategy);
        this.eventRepository.save(event);
    }

    private Log addForLaterHandling(Log log) {
        return unhandledLogs.put(log.getEventId(), log);
    }

    @Override
    public void finish() {
        eventRepository.flush();
    }
}
