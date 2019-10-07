package loganalyzer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

final class ThreadedLogHandle implements LogHandle {
    private final LogHandle next;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    public ThreadedLogHandle(LogHandle next) {
        this.next = next;
    }

    @Override
    public void handle(Log log) {
        executor.execute(() -> this.next.handle(log));
    }

    @Override
    public void finish() {
        executor.execute(this.next::finish);
        shutdown();
    }

    private void shutdown() {
        try {
            executor.shutdown();
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }
}
