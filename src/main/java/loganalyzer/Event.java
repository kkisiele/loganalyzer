package loganalyzer;

import java.util.Objects;

public final class Event {
    private final String id;
    private final long durationInMilliseconds;
    private final String type;
    private final String host;
    private final boolean alert;

    private Event(String id, long durationInMilliseconds, String type, String host, boolean alert) {
        this.id = id;
        this.durationInMilliseconds = durationInMilliseconds;
        this.type = type;
        this.host = host;
        this.alert = alert;
    }

    public String getId() {
        return this.id;
    }

    public long getDurationInMilliseconds() {
        return this.durationInMilliseconds;
    }

    public String getType() {
        return this.type;
    }

    public String getHost() {
        return this.host;
    }

    public boolean isAlert() {
        return this.alert;
    }

    @Override
    public boolean equals(Object another) {
        if(another instanceof Event) {
            Event that = (Event) another;
            return this.durationInMilliseconds == that.durationInMilliseconds &&
                    this.alert == that.alert &&
                    this.id.equals(that.id) &&
                    Objects.equals(this.type, that.type) &&
                    Objects.equals(this.host, that.host);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.host);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id='" + id + '\'' +
                ", durationInMilliseconds=" + durationInMilliseconds +
                ", type='" + type + '\'' +
                ", host='" + host + '\'' +
                ", alert=" + alert +
                '}';
    }

    public Event alertOn() {
        return new Builder().fromEvent(this).withAlert(true).build();
    }

    public static class Builder {
        private String id;
        private long durationInMilliseconds;
        private String type;
        private String host;
        private boolean alert;

        public Builder fromLog(Log log) {
            this.id = log.getEventId();
            this.type = log.getType();
            this.host = log.getHost();
            return this;
        }

        public Builder fromEvent(Event event) {
            this.id = event.getId();
            this.durationInMilliseconds = event.getDurationInMilliseconds();
            this.type = event.getType();
            this.host = event.getHost();
            this.alert = event.isAlert();
            return this;
        }

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withDurationInMilliseconds(long durationInMilliseconds) {
            this.durationInMilliseconds = durationInMilliseconds;
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

        public Builder withAlert(boolean alert) {
            this.alert = alert;
            return this;
        }

        public Event build() {
            return new Event(this.id, this.durationInMilliseconds, this.type, this.host, this.alert);
        }
    }
}
