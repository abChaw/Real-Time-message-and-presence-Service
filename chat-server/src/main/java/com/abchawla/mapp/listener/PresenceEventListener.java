package com.abchawla.mapp.listener;

import com.abchawla.mapp.core.PresenceEvent;
import com.abchawla.mapp.core.PresenceEvent.Status;
import com.abchawla.mapp.publisher.EventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.time.Instant;

@Component
public class    PresenceEventListener {
    private final EventPublisher eventPublisher;
    private final SimpMessagingTemplate messagingTemplate;

    public PresenceEventListener(EventPublisher eventPublisher,
                                 SimpMessagingTemplate messagingTemplate) {
        this.eventPublisher = eventPublisher;
        this.messagingTemplate = messagingTemplate;
    }

    @EventListener
    public void handleSessionConnected(SessionConnectedEvent event) {
        Principal user  = event.getUser();
        if (user == null) {
            return; // no user info—skip offline broadcast
        }
        PresenceEvent pe = new PresenceEvent(user.getName(), Status.ONLINE, Instant.now());
        eventPublisher.publish(pe);
        messagingTemplate.convertAndSend("/topic/presence", pe);
    }

    @EventListener
    public void handleSessionDisconnected(SessionDisconnectEvent event) {
        Principal user = event.getUser();
        if (user == null) {
            return; // no user info—skip offline broadcast
        }
        PresenceEvent pe = new PresenceEvent(user.getName(), Status.OFFLINE, Instant.now());
        eventPublisher.publish(pe);
        messagingTemplate.convertAndSend("/topic/presence", pe);
    }
}
