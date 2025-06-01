package com.abchawla.mapp.controllers;

import com.abchawla.mapp.session.WebSocketSessionRegistry;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController {
    private final WebSocketSessionRegistry sessionRegistry;

    public UserController(WebSocketSessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    /**
     * Simple login: client POSTs { "username": "Alice" }.
     * We store it in the HTTP session so that if you want to tie REST calls to a user,
     * you can—but mostly this just confirms the username exists.
     */
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody Map<String, String> body, HttpSession session) {
        String username = body.get("username");
        if (username == null || username.isBlank()) {
            return ResponseEntity.badRequest().build();
        }
        session.setAttribute("username", username.trim());
        return ResponseEntity.ok().build();
    }

    /** Return a list of all currently connected/logged-in usernames. */
    @GetMapping("/online-users")
    public ResponseEntity<List<String>> onlineUsers() {
        List<String> users = sessionRegistry.getAllUsernames();
        return ResponseEntity.ok(users);
    }
}
