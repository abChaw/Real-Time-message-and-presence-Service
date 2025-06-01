package com.abchawla.mapp.controllers;

import com.abchawla.mapp.core.ChatMessage;
import com.abchawla.mapp.service.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;
import java.util.List;

public class HistoryController {
    private final MessageService messageService;

    public HistoryController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * GET /api/history/{roomId}
     * Returns the last 50 messages for the given room in chronological order.
     */
    @GetMapping("/history/{roomId}")
    public List<ChatMessage> getHistory(@PathVariable String roomId) {
        List<ChatMessage> history = messageService.fetchHistory(roomId);
        Collections.reverse(history); // fetchHistory returns newest first; reverse to oldest→newest
        return history;
    }


}
