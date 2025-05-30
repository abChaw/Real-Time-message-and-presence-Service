package com.abchawla.mapp.event;

import com.abchawla.mapp.publisher.EventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Delegates to Spring’s ApplicationEventPublisher.
 */
@Component
public class SpringEventPublisher implements EventPublisher {
    private final ApplicationEventPublisher publisher;

    public SpringEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(Object event) {
        publisher.publishEvent(event);
    }
}
