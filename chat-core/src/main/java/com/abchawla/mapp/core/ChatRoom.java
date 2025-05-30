package com.abchawla.mapp.core;

public interface ChatRoom {
    /** A user joins this room’s participant set. */
    void join(String username);

    /** A user leaves the room. */
    void leave(String username);

    /** Dispatch a message to all participants. */
    void dispatch(ChatMessage message);
}
