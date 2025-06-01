package com.abchawla.mapp.listener;

import com.abchawla.mapp.session.WebSocketSessionRegistry;
import com.abchawla.mapp.core.PresenceEvent;
import com.abchawla.mapp.core.PresenceEvent.Status;
import com.abchawla.mapp.publisher.EventPublisher;
import com.abchawla.mapp.service.MessageService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.Instant;

@Component
public class PresenceEventListener {

    private final EventPublisher eventPublisher;
    private final SimpMessagingTemplate messagingTemplate;
    private final WebSocketSessionRegistry sessionRegistry;
    private final MessageService messageService;

    public PresenceEventListener(EventPublisher ep,
                                 SimpMessagingTemplate mt,
                                 WebSocketSessionRegistry reg,
                                 MessageService msgSvc) {
        this.eventPublisher    = ep;
        this.messagingTemplate = mt;
        this.sessionRegistry   = reg;
        this.messageService    = msgSvc;
    }

    // 1) Capture username in the raw CONNECT frame
    @EventListener
    public void handleSessionConnect(SessionConnectEvent event) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String username  = accessor.getFirstNativeHeader("username");
        if (username == null || username.isBlank()) {
            username = "guest";
        }
        sessionRegistry.register(sessionId, username);
    }

    // 2) Once fully connected, broadcast presence ONLINE and replay history
    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        SimpMessageHeaderAccessor accessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String sessionId = accessor.getSessionId();
        String username  = sessionRegistry.findBySessionId(sessionId);
        if (username == null) return;

        // Broadcast “ONLINE” to /topic/presence
        PresenceEvent pe = new PresenceEvent(username, Status.ONLINE, Instant.now());
        eventPublisher.publish(pe);
        messagingTemplate.convertAndSend("/topic/presence", pe);

        // Replay history for room1
        String roomId = "room1";
        messageService.fetchHistory(roomId)
                .forEach(msg ->
                        messagingTemplate.convertAndSend("/topic/room/" + roomId, msg)
                );
    }

    // 3) When a client disconnects, broadcast OFFLINE
    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        String user = sessionRegistry.removeBySessionId(sessionId);
        if (user == null) return;

        PresenceEvent pe = new PresenceEvent(user, Status.OFFLINE, Instant.now());
        eventPublisher.publish(pe);
        messagingTemplate.convertAndSend("/topic/presence", pe);
    }
}
