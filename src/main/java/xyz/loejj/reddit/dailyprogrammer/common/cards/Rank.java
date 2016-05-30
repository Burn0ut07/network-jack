package xyz.loejj.reddit.dailyprogrammer.common.cards;

import java.util.Optional;

/**
 * Creation date: 2016-05-29.
 * Author: jjauregui
 * <p/>
 * Copyright 2015, Asset Science LLC. All rights reserved.
 */
public enum Rank {
    Two("2", 2),
    Three("3", 3),
    Four("4", 4),
    Five("5", 5),
    Six("6", 6),
    Seven("7", 7),
    Eight("8", 8),
    Nine("9", 9),
    Jack("Jack", 10),
    Queen("Queen", 10),
    King("King", 10),
    Ace("Ace", 11);

    private int value;
    private String alias;

    private Rank(String alias, int value) {
        this.alias = alias;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public String getAlias() {
        return alias;
    }

    public static Optional<Rank> rankByAlias(String alias) {
        for (Rank rank : Rank.values()) {
            if (rank.getAlias().equals(alias)) {
                return Optional.of(rank);
            }
        }

        return Optional.empty();
    }
}
