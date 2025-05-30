package com.abchawla.mapp.service;

import com.abchawla.mapp.core.ChatMessage;
import com.abchawla.mapp.entity.MessageEntity;
import com.abchawla.mapp.repo.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {
    private final MessageRepository repo;

    public MessageService(MessageRepository repo) {
        this.repo = repo;
    }

    public ChatMessage save(ChatMessage msg) {
        MessageEntity e = new MessageEntity(
                msg.getFromUser(),
                msg.getRoomId(),
                msg.getContent(),
                msg.getTimestamp()
        );
        MessageEntity saved = repo.save(e);
        // map back to ChatMessage if you need the generated ID (not shown)
        return msg;
    }

    public List<ChatMessage> fetchHistory(String roomId) {

        return repo.findTop50ByRoomIdOrderByTimestampDesc(roomId)
                .stream()
                .map(e -> new ChatMessage(
                        e.getFromUser(),
                        e.getRoomId(),
                        e.getContent(),
                        e.getTimestamp()))
                .collect(Collectors.toList());
    }
}
