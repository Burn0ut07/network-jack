package xyz.loejj.reddit.dailyprogrammer.common.protocol.messages;

import xyz.loejj.reddit.dailyprogrammer.server.BlackjackPlayer;

/**
 * Creation date: 2016-05-28.
 * Author: jjauregui
 * <p/>
 * Copyright 2015, Asset Science LLC. All rights reserved.
 */
public interface MessageHandler {
    default void handle(Message message, BlackjackPlayer player) {
        throw new RuntimeException("Unhandled Message: " + message);
    }
    default void handle(UnparseableMessage message) {
        System.err.println("Could not parse message: " + message);
    }
    void handle(Ping message);
    void handle(Pong message, BlackjackPlayer player);
    void handle(Name message);
    void handle(SetDisplayName message, BlackjackPlayer player);
    void handle(Start message, BlackjackPlayer player);
    void handle(ChoiceQuery message);
    void handle(Take message, BlackjackPlayer player);
    void handle(Pass message, BlackjackPlayer player);
    void handle(Play message, BlackjackPlayer player);
    void handle(GameOver message);
}
