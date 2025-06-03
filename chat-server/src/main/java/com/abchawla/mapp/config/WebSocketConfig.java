package com.abchawla.mapp.config;

import com.abchawla.mapp.security.JwtUtil;
import com.abchawla.mapp.session.WebSocketSessionRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import jakarta.servlet.http.HttpSession;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtUtil jwtUtil;
    private final WebSocketSessionRegistry sessionRegistry;

    public WebSocketConfig(JwtUtil jwtUtil, WebSocketSessionRegistry reg) {
        this.jwtUtil = jwtUtil;
        this.sessionRegistry = reg;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat")
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(ServerHttpRequest request,
                                                      WebSocketHandler wsHandler,
                                                      Map<String, Object> attributes) {
                        // Extract JWT from “Authorization” query param or header
                        List<String> authHeaders = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
                        String token = null;
                        if (authHeaders != null && !authHeaders.isEmpty()
                                && authHeaders.get(0).startsWith("Bearer ")) {
                            token = authHeaders.get(0).substring(7);
                        } else {
                            // SockJS clients might send token as a query parameter “?token=…”
                            String uri = request.getURI().toString();
                            if (uri.contains("token=")) {
                                String[] parts = uri.split("token=");
                                token = parts[1].split("&")[0];
                            }
                        }
                        if (token == null) {
                            return null;
                        }
                        String username = jwtUtil.validateAndGetUsername(token);
                        if (username == null) {
                            return null;
                        }
                        // Return a Principal so Spring can use it in STOMP events
                        return () -> username;
                    }
                })
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Enable user destination prefix for private messages
        registry.setUserDestinationPrefix("/user");
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }
}
