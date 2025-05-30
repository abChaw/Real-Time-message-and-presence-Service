// ChatCoreDtoTest.java
package com.abchawla.mapp.core;

import org.junit.Test;
import java.time.Instant;

import static org.junit.Assert.*;

public class ChatCoreDtoTest {

    @Test
    public void chatMessage_gettersAndSetters_work() {
        Instant now = Instant.now();
        ChatMessage msg = new ChatMessage("alice", "room1", "hello", now);

        assertEquals("alice", msg.getFromUser());
        assertEquals("room1", msg.getRoomId());
        assertEquals("hello", msg.getContent());
        assertEquals(now, msg.getTimestamp());

        msg.setContent("hi");
        assertEquals("hi", msg.getContent());
    }

    @Test
    public void presenceEvent_equalityAndHashCode() {
        Instant now = Instant.now();
        PresenceEvent e1 = new PresenceEvent("bob", PresenceEvent.Status.ONLINE, now);
        PresenceEvent e2 = new PresenceEvent("bob", PresenceEvent.Status.ONLINE, now);

        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }
}
