package xyz.loejj.reddit.dailyprogrammer.common.protocol.messages;

import xyz.loejj.reddit.dailyprogrammer.server.BlackjackPlayer;

/**
 * Creation date: 2016-05-28.
 * Author: jjauregui
 * <p/>
 * Copyright 2015, Asset Science LLC. All rights reserved.
 */
public class UnparseableMessage implements Message {
    private String message;

    public UnparseableMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void accept(MessageHandler handler, BlackjackPlayer player) {
        handler.handle(this);
    }

    public String toString() {
        return message;
    }
}
