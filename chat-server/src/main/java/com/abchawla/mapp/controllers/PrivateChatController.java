package com.abchawla.mapp.controllers;
import com.abchawla.mapp.core.ChatMessage;
import com.abchawla.mapp.entity.MessageEntity;
import com.abchawla.mapp.repo.MessageRepository;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestController
public class PrivateChatController {
    private final SimpMessagingTemplate broker;
    private final MessageRepository messageRepo;

    public PrivateChatController(SimpMessagingTemplate broker,
                                 MessageRepository repo) {
        this.broker = broker;
        this.messageRepo = repo;
    }

    /**
     * Send a private message from one user to another.
     * Client must SEND to /app/chat.private
     * with payload { toUser, content, timestamp }.
     * The server wraps it into a ChatMessage with fromUser from Principal.
     * Then broker sends to /user/{toUser}/queue/private
     */
    @MessageMapping("/chat.private")
    public void privateChat(@Payload Map<String, String> payload,
                            @AuthenticationPrincipal String fromUser) {

        String toUser = payload.get("toUser");
        String content = payload.get("content");
        String timestamp = payload.get("timestamp");

        // Save in DB with roomId = "private:{sortedUsers}"
        String roomId = makePrivateRoomId(fromUser, toUser);
        MessageEntity e = new MessageEntity(fromUser, roomId, content, Instant.parse(timestamp));
        messageRepo.save(e);

        ChatMessage msg = new ChatMessage(fromUser, roomId, content, Instant.parse(timestamp));
        broker.convertAndSendToUser(toUser, "/queue/private", msg);
    }

    private String makePrivateRoomId(String a, String b) {
        // e.g. "private:alice|bob" (alphabetical to ensure uniqueness)
        if (a.compareTo(b) < 0) {
            return "private:" + a + "|" + b;
        } else {
            return "private:" + b + "|" + a;
        }
    }
}
