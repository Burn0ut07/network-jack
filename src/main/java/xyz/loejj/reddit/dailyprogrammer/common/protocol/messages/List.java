package xyz.loejj.reddit.dailyprogrammer.common.protocol.messages;

import xyz.loejj.reddit.dailyprogrammer.server.BlackjackPlayer;

import java.util.stream.Stream;

/**
 * Creation date: 2016-05-28.
 * Author: jjauregui
 * <p/>
 * Copyright 2015, Asset Science LLC. All rights reserved.
 */
public class List implements Message {
    @Override
    public void accept(MessageHandler handler, BlackjackPlayer player) {
        handler.handle(this, player);
    }

    public String toString() {
        return "LIST";
    }

    public static Stream<Message> parse(String message) {
        if (message.equals("LIST")) {
            return Stream.of(new List());
        } else {
            return Stream.empty();
        }
    }
}
