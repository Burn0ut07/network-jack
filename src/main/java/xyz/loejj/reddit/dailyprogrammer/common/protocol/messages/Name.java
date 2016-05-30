package xyz.loejj.reddit.dailyprogrammer.common.protocol.messages;

import xyz.loejj.reddit.dailyprogrammer.server.BlackjackPlayer;

import java.util.stream.Stream;

/**
 * Creation date: 2016-05-28.
 * Author: jjauregui
 * <p/>
 * Copyright 2015, Asset Science LLC. All rights reserved.
 */
public class Name implements Message {
    private String name;

    public Name(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return "NAME: " + name;
    }

    public static Stream parse(String message) {
        if (!message.startsWith("NAME: ")) {
            return Stream.empty();
        }

        String name = message.substring(message.indexOf(' ') + 1);

        return Stream.of(new Name(name));
    }

    @Override
    public void accept(MessageHandler handler, BlackjackPlayer player) {
        handler.handle(this);
    }
}
