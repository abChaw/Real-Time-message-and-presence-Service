package com.abchawla.mapp.utility;

import com.abchawla.mapp.service.MessageService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@Component
public class HistoryLoader  {

    private final MessageService messageService;
    private final SimpMessagingTemplate broker;

    public HistoryLoader(MessageService messageService,
                         SimpMessagingTemplate broker) {
        this.messageService = messageService;
        this.broker = broker;
    }

    @EventListener
    public void onSessionConnected(SessionConnectedEvent evt) {
        String roomId = "room1"; // or derive from headers
        messageService.fetchHistory(roomId)
                .forEach(msg ->
                        broker.convertAndSend("/topic/room/" + roomId, msg));
    }
}
