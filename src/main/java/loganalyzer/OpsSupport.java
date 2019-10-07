package loganalyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class OpsSupport {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private long processingStartTime;

    public void startedProcessing() {
        if(log.isInfoEnabled()) {
            processingStartTime = System.currentTimeMillis();
            log.info("Started processing log file...");
        }
    }

    public void finishedProcessing() {
        if(log.isInfoEnabled()) {
            long durationInMilliseconds = System.currentTimeMillis() - processingStartTime;
            log.info("Finished processing log file in " + formatTime(durationInMilliseconds));
        }
    }

    private String formatTime(long durationInMilliseconds) {
        long durationInSeconds = durationInMilliseconds / 1000;
        if(durationInSeconds > 0) {
            return durationInSeconds == 1 ? "one second" : String.format("%d seconds", durationInSeconds);
        }

        return String.format("%d milliseconds", durationInMilliseconds);
    }

    public void invalidLogFormat(String event) {
        if(log.isWarnEnabled()) {
            log.warn("Provided event format cannot be de-serialized: " + event);
        }
    }

    public void savingEvent(Event event) {
        if(log.isDebugEnabled()) {
            log.debug("Saving event to database: " + event);
        }
    }
}
