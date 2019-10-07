package loganalyzer;

import java.util.List;

public interface EventRepository {
    void save(List<Event> events);
}
