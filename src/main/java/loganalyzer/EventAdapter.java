package loganalyzer;

import loganalyzer.Event.Builder;
import loganalyzer.Log.State;

import java.util.List;
import java.util.stream.Collectors;

final class EventAdapter {
    public static Event toEvent(List<Log> logs, FlagEventStrategy flagEventStrategy) {
        Log started = logInState(logs, State.STARTED);
        Log finished = logInState(logs, State.FINISHED);

        Event event = new Builder()
                .fromLog(started)
                .withDurationInMilliseconds(finished.getTimestamp() - started.getTimestamp())
                .build();
        if(flagEventStrategy.isFlagged(event)) {
            event = event.alertOn();
        }
        return event;
    }

    private static Log logInState(List<Log> logs, State state) {
        List<Log> filteredLogs = logs.stream()
                .filter(log -> log.getState() == state)
                .collect(Collectors.toList());
        return uniqueLogInState(filteredLogs, state);
    }

    private static Log uniqueLogInState(List<Log> logs, State state) {
        if(logs.size() == 0) {
            throw new RuntimeException("No log entry in [" + state + "] state");
        } else if(logs.size() > 1) {
            throw new RuntimeException("There are " + logs.size() + " log entries in [" + state + "] state");
        }
        return logs.get(0);
    }
}
