package loganalyzer;

public final class Log {
    private final String id;
    private final State state;
    private final long timestamp;
    private final String type;
    private final String host;

    public enum State {
        STARTED,
        FINISHED
    }

    private Log(String id, State state, long timestamp, String type, String host) {
        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
        this.type = type;
        this.host = host;
    }

    public String getEventId() {
        return id;
    }

    public State getState() {
        return state;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public String getHost() {
        return host;
    }

    public static class Builder {
        private String id;
        private State state;
        private long timestamp;
        private String type;
        private String host;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withState(State state) {
            this.state = state;
            return this;
        }

        public Builder withTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder withType(String type) {
            this.type = type;
            return this;
        }

        public Builder withHost(String host) {
            this.host = host;
            return this;
        }

        public Log build() {
            return new Log(this.id, this.state, this.timestamp, this.type, this.host);
        }
    }
}
