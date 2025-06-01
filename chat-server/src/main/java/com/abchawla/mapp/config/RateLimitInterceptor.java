package com.abchawla.mapp.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {
    private static final int MAX_PER_SECOND = 5;
    private Instant windowStart = Instant.now();
    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public boolean preHandle(HttpServletRequest req,
                             HttpServletResponse resp,
                             Object handler) throws Exception {
        if (!"/app/chat.send".equals(req.getRequestURI())) {
            return true;
        }
        synchronized (this) {
            Instant now = Instant.now();
            if (now.isAfter(windowStart.plusSeconds(1))) {
                windowStart = now;
                count.set(0);
            }
            if (count.incrementAndGet() > MAX_PER_SECOND) {
                resp.setStatus(429);
                resp.getWriter().write("Rate limit exceeded");
                return false;
            }
            return true;
        }
    }
}
