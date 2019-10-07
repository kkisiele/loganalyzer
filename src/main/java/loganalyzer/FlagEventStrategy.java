package loganalyzer;

interface FlagEventStrategy {
    boolean isFlagged(Event event);

    FlagEventStrategy LONG_EVENT = new FlagEventStrategy() {
        private static final int ALERT_THRESHOLD_IN_MILLISECONDS = 4;

        @Override
        public boolean isFlagged(Event event) {
            return event.getDurationInMilliseconds() > ALERT_THRESHOLD_IN_MILLISECONDS;
        }
    };
}
