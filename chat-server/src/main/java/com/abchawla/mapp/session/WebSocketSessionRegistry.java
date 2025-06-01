package com.abchawla.mapp.session;


import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

    public String findBySessionId(String sessionId) {
        return sessions.get(sessionId);
    }

    public List<String> getAllUsernames() {
        return sessions.values().stream().distinct().collect(Collectors.toList());
    }
}
