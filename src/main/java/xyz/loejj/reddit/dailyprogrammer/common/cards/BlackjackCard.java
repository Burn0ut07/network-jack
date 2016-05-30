package xyz.loejj.reddit.dailyprogrammer.common.cards;

/**
 * Creation date: 2016-05-29.
 * Author: jjauregui
 * <p/>
 * Copyright 2015, Asset Science LLC. All rights reserved.
 */
public class BlackjackCard extends Card {
    protected boolean hidden;

    public BlackjackCard(Rank rank, Suit suit) {
        super(rank, suit);
        this.hidden = true;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
