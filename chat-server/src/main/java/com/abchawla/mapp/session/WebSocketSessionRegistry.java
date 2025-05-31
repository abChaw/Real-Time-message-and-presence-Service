package com.abchawla.mapp;


import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketSessionRegistry {

    // sessionId → username
    private final Map<String,String> sessions = new ConcurrentHashMap<>();

    /** Call on connect, after you’ve extracted the username. */
    public void register(String sessionId, String username) {
        sessions.put(sessionId, username);
    }

    /** Call on disconnect to remove and return the username. */
    public String removeBySessionId(String sessionId) {
        return sessions.remove(sessionId);
    }
}
