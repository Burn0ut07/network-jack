package xyz.loejj.reddit.dailyprogrammer.server;

import java.util.List;

/**
 * Creation date: 2016-05-29.
 * Author: jjauregui
 * <p/>
 * Copyright 2015, Asset Science LLC. All rights reserved.
 */
public class GameResults {
    protected List<BlackjackPlayer> winners;
    protected int winningScore;

    public GameResults(List<BlackjackPlayer> winners, int winningScore) {
        this.winners = winners;
        this.winningScore = winningScore;
    }

    public List<BlackjackPlayer> getWinners() {
        return winners;
    }

    public int getWinningScore() {
        return winningScore;
    }
}
