package loganalyzer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class InMemoryEventRepository implements EventRepository{
    private final List<Event> events = new LinkedList<>();

    @Override
    public void save(List<Event> events) {
        this.events.addAll(events);
    }

    public List<Event> events() {
        return new ArrayList<>(this.events);
    }
}
