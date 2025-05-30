package com.abchawla.mapp.controllers;
import com.abchawla.mapp.core.ChatMessage;
import com.abchawla.mapp.core.ChatRoom;
import com.abchawla.mapp.service.MessageService;
import org.springframework.stereotype.Controller;
import org.springframework.messaging.handler.annotation.MessageMapping;


@Controller
public class ChatController {
    private final ChatRoom chatRoom;
    private final MessageService messageService;

    public ChatController(ChatRoom chatRoom, MessageService messageService) {
        this.chatRoom = chatRoom;
        this.messageService = messageService;
    }

    /** STOMP destination: /app/chat.send */
    @MessageMapping("/chat.send")
    public void send(ChatMessage message) {

     //    1) persist
        messageService.save(message);
        // 2) dispatch
        chatRoom.dispatch(message);
    }
}
