package xyz.loejj.reddit.dailyprogrammer.common.protocol.messages;

import xyz.loejj.reddit.dailyprogrammer.server.BlackjackPlayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Creation date: 2016-05-29.
 * Author: jjauregui
 * <p/>
 * Copyright 2015, Asset Science LLC. All rights reserved.
 */
public class Start implements Message {
    public static final String REPR = "START";

    @Override
    public void accept(MessageHandler handler, BlackjackPlayer player) {
        handler.handle(this, player);
    }

    @Override
    public String toString() {
        return REPR;
    }

    public static Stream<Message> parse(String message) {
        if (message.equals(REPR)) {
            return Stream.of(new Start());
        } else {
            return Stream.empty();
        }
    }
}
