// ChatMessage.java
package com.abchawla.mapp.core;

import java.time.Instant;
import java.util.Objects;

public class ChatMessage {
    private String fromUser;
    private String roomId;
    private String content;
    private Instant timestamp;

    public ChatMessage() { }

    public ChatMessage(String fromUser, String roomId, String content, Instant timestamp) {
        this.fromUser = fromUser;
        this.roomId = roomId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getFromUser() {
        return fromUser;
    }
    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getRoomId() {
        return roomId;
    }
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
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
        if (!(o instanceof ChatMessage)) return false;
        ChatMessage that = (ChatMessage) o;
        return Objects.equals(fromUser, that.fromUser)
                && Objects.equals(roomId, that.roomId)
                && Objects.equals(content, that.content)
                && Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromUser, roomId, content, timestamp);
    }

    @Override
    public String toString() {
        return "ChatMessage{" +
                "fromUser='" + fromUser + '\'' +
                ", roomId='" + roomId + '\'' +
                ", content='" + content + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
