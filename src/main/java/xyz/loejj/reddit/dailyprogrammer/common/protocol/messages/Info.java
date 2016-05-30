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
public class Info implements Message {
    private static final Pattern INFO_PATTERN = Pattern.compile("INFO (.+)");

    private String info;

    public Info(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }

    @Override
    public String toString() {
        return "INFO " + info;
    }

    @Override
    public void accept(MessageHandler handler, BlackjackPlayer player) {
        handler.handle(this);
    }

    public static Stream<Message> parse(String message) {
        Matcher matcher = INFO_PATTERN.matcher(message);
        if (!matcher.matches()) {
            return Stream.empty();
        }

        return Stream.of(new Info(matcher.group(1)));
    }
}
