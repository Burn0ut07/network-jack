package xyz.loejj.reddit.dailyprogrammer.common.protocol.messages;

import xyz.loejj.reddit.dailyprogrammer.server.BlackjackPlayer;

import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Creation date: 2016-05-29.
 * Author: jjauregui
 * <p/>
 * Copyright 2015, Asset Science LLC. All rights reserved.
 */
public class GameOver implements Message {
    private static final Pattern GAME_OVER_PATTERN = Pattern.compile("GAMEOVER (0|(\\d+)( .+)+)");

    private java.util.List<String> winners;
    private int winningScore;

    public GameOver(List<String> winners, int winningScore) {
        this.winners = winners;
        this.winningScore = winningScore;
    }

    public List<String> getWinners() {
        return winners;
    }

    public int getWinningScore() {
        return winningScore;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("GAMEOVER ");
        builder.append(winningScore);

        for (String winner : winners) {
            builder.append(' ');
            builder.append(winner);
        }

        return builder.toString();
    }

    @Override
    public void accept(MessageHandler handler, BlackjackPlayer player) {
        handler.handle(this);
    }

    public static Stream<Message> parse(String message) {
        Matcher matcher = GAME_OVER_PATTERN.matcher(message);
        if (!matcher.matches()) {
            return Stream.empty();
        }

        if (matcher.group(2) != null) {
            int winningScore = Integer.parseInt(matcher.group(2));
            List<String> winners = Arrays.asList(matcher.group(3).split(" "));
            return Stream.of(new GameOver(winners, winningScore));
        } else {
            return Stream.of(new GameOver(Collections.EMPTY_LIST, 0));
        }
    }
}
