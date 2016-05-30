package xyz.loejj.reddit.dailyprogrammer.common.protocol.messages;

import xyz.loejj.reddit.dailyprogrammer.server.BlackjackPlayer;

import java.util.stream.Stream;

/**
 * Creation date: 2016-05-29.
 * Author: jjauregui
 * <p/>
 * Copyright 2015, Asset Science LLC. All rights reserved.
 */
public class ChoiceQuery implements Message {
    public static final String REPR = "CHOICE";

    @Override
    public void accept(MessageHandler handler, BlackjackPlayer player) {
        handler.handle(this);
    }

    @Override
    public String toString() {
        return REPR;
    }

    public static Stream<Message> parse(String message) {
        if (message.equals(REPR)) {
            return Stream.of(new ChoiceQuery());
        } else {
            return Stream.empty();
        }
    }
}
