package loganalyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class InMemoryLogRepository implements LogRepository {
    private final List<Log> logs;

    public InMemoryLogRepository(Log... logs) {
        this(Arrays.asList(logs));
    }

    public InMemoryLogRepository(List<Log> logs) {
        this.logs = new ArrayList<>(logs);
    }

    @Override
    public void forEachFoundLog(Consumer<Log> consumer) {
        logs.forEach(consumer);
    }
}
