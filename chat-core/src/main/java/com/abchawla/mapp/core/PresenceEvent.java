// PresenceEvent.java
package com.abchawla.mapp.core;

import java.time.Instant;
import java.util.Objects;

public class PresenceEvent {
    public enum Status {
        ONLINE, OFFLINE
    }

    private String username;
    private Status status;
    private Instant timestamp;

    public PresenceEvent() { }

    public PresenceEvent(String username, Status status, Instant timestamp) {
        this.username = username;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PresenceEvent)) return false;
        PresenceEvent that = (PresenceEvent) o;
        return Objects.equals(username, that.username)
                && status == that.status
                && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, status, timestamp);
    }

    @Override
    public String toString() {
        return "PresenceEvent{" +
                "username='" + username + '\'' +
                ", status=" + status +
                ", timestamp=" + timestamp +
                '}';
    }
}
