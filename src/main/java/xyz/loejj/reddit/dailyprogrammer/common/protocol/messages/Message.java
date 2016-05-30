package xyz.loejj.reddit.dailyprogrammer.common.protocol.messages;

import xyz.loejj.reddit.dailyprogrammer.server.BlackjackPlayer;

/**
 * Creation date: 2016-05-28.
 * Author: jjauregui
 * <p/>
 * Copyright 2015, Asset Science LLC. All rights reserved.
 */
public interface Message {
    void accept(MessageHandler handler, BlackjackPlayer player);
    default String getInfoDescription(BlackjackPlayer player) {
        return toString();
    }
    default String toWireFormat() {
        return toString() + "\n";
    }
}
