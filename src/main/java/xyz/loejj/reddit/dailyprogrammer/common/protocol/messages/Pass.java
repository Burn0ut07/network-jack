package xyz.loejj.reddit.dailyprogrammer.common.protocol.messages;

import xyz.loejj.reddit.dailyprogrammer.server.BlackjackPlayer;

import java.util.stream.Stream;

/**
 * Creation date: 2016-05-29.
 * Author: jjauregui
 * <p/>
 * Copyright 2015, Asset Science LLC. All rights reserved.
 */
public class Pass implements Message {
    public static final String REPR = "PASS";

    @Override
    public void accept(MessageHandler handler, BlackjackPlayer player) {
        handler.handle(this, player);
    }

    @Override
    public String toString() {
        return REPR;
    }

    @Override
    public String getInfoDescription(BlackjackPlayer player) {
        return player.getName() + " has passed.";
    }

    public static Stream<Message> parse(String message) {
        if (message.equals(REPR)) {
            return Stream.of(new Pass());
        } else {
            return Stream.empty();
        }
    }
}
