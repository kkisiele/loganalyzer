package loganalyzer;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

public interface LogRepository {
    void forEachFoundLog(Consumer<Log> consumer);

    default List<Log> findAll() {
        List<Log> result = new LinkedList<>();
        forEachFoundLog(log -> result.add(log));
        return result;
    }
}
