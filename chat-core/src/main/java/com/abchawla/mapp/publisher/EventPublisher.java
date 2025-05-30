// EventPublisher.java
package com.abchawla.mapp.publisher;

/**
 * Simple abstraction for publishing domain events.
 */
public interface EventPublisher {
    /**
     * Publish any event object (e.g. ChatMessage, PresenceEvent).
     */
    void publish(Object event);
}
