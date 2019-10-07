package loganalyzer.infrastructure.log;

import loganalyzer.Log;
import loganalyzer.LogRepository;
import loganalyzer.OpsSupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public final class JsonLogRepository implements LogRepository {
    private final JsonLogAdapter adapter;
    private final BufferedReader reader;

    public JsonLogRepository(InputStream inputStream, OpsSupport opsSupport) {
        adapter = new JsonLogAdapter(opsSupport);
        reader = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public void forEachFoundLog(Consumer<Log> consumer) {
        Log log = null;
        while((log = readLog()) != null) {
            consumer.accept(log);
        }
    }

    private Log readLog() {
        String line = null;
        while((line = readLine()) != null) {
            Log log = adapter.toLog(line);
            if(log != null) {
                return log;
            }
        }
        return null;
    }

    private String readLine() {
        try {
            return reader.readLine();
        } catch (IOException ex) {
            throw new RuntimeException("Error while reading from file", ex);
        }
    }
}
