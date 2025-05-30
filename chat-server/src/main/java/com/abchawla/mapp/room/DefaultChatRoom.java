package com.abchawla.mapp.room;

import com.abchawla.mapp.core.ChatMessage;
import com.abchawla.mapp.core.ChatRoom;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DefaultChatRoom implements ChatRoom {
    private final Set<String> participants = ConcurrentHashMap.newKeySet();
    private final SimpMessagingTemplate broker;

    public DefaultChatRoom(SimpMessagingTemplate broker) {
        this.broker = broker;
    }

    @Override
    public void join(String username) {
        participants.add(username);
        // Optionally notify others…
    }

    @Override
    public void leave(String username) {
        participants.remove(username);
        // Optionally notify others…
    }

    @Override
    public void dispatch(ChatMessage message) {
        // Broadcast to /topic/room/{roomId}
        broker.convertAndSend("/topic/room/" + message.getRoomId(), message);
    }
}
