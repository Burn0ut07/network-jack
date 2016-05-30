package xyz.loejj.reddit.dailyprogrammer.common.protocol.messages;

import xyz.loejj.reddit.dailyprogrammer.server.BlackjackPlayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Creation date: 2016-05-28.
 * Author: jjauregui
 * <p/>
 * Copyright 2015, Asset Science LLC. All rights reserved.
 */
public class SetDisplayName implements Message {
    public static final Pattern NAME_CHANGE_PATTERN = Pattern.compile("^NAME: (\\S+) (\\S+)$");

    private String userId;
    private String displayName;

    public SetDisplayName(String userId, String displayName) {
        this.userId = userId;
        this.displayName = displayName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public void accept(MessageHandler handler, BlackjackPlayer player) {
        handler.handle(this, player);
    }

    @Override
    public String toString() {
        return String.format("NAME: %s %s", userId, displayName);
    }

    public static Stream parse(String message) {
        Matcher matcher = NAME_CHANGE_PATTERN.matcher(message);
        if (!matcher.matches()) {
            return Stream.empty();
        }

        return Stream.of(new SetDisplayName(matcher.group(1), matcher.group(2)));
    }
}
