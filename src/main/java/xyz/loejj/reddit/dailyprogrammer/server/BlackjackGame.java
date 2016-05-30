package xyz.loejj.reddit.dailyprogrammer.server;

import xyz.loejj.reddit.dailyprogrammer.common.cards.BlackjackCard;
import xyz.loejj.reddit.dailyprogrammer.common.cards.Rank;
import xyz.loejj.reddit.dailyprogrammer.common.cards.Suit;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Creation date: 2016-05-29.
 * Author: jjauregui
 * <p/>
 * Copyright 2015, Asset Science LLC. All rights reserved.
 */
public class BlackjackGame {
    public static final int BLACKJACK = 21;

    private List<BlackjackPlayer> players;
    private List<BlackjackPlayer> waitingPlayers;
    private List<BlackjackPlayer> spectators;
    private boolean inGame;
    private int currentPlayer;
    private Random random;

    public BlackjackGame() {
        this.players = new ArrayList<>();
        this.waitingPlayers = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.inGame = false;
        this.currentPlayer = 0;
        this.random = new Random();
    }

    public void addPlayer(BlackjackPlayer player) {
        if (!inGame) {
            players.add(player);
        } else {
            waitingPlayers.add(player);
        }
    }

    public void addSpectator(BlackjackPlayer spectator) {
        spectators.add(spectator);
    }

    public boolean removePlayer(BlackjackPlayer player) {
        return players.remove(player);
    }

    public Optional<BlackjackPlayer> getPlayer(String username) {
        return players.stream()
                .filter(player -> player.getName().equals(username))
                .findAny();
    }

    public boolean isNewGameReady() {
        if (inGame) {
            return false;
        }

        inGame = players.stream().allMatch(player -> player.isReady());
        return inGame;
    }

    public Optional<BlackjackPlayer> getNextPlayer() {
        BlackjackPlayer player = players.get(currentPlayer);
        if (player.didPass() || player.didBust()) {
            currentPlayer++;
            if (currentPlayer < players.size()) {
                player = players.get(currentPlayer);
            } else {
                player = null;
            }
        }
        return Optional.ofNullable(player);
    }

    public List<BlackjackPlayer> getPlayers() {
        return players;
    }

    public boolean isGameOver() {
        return players.parallelStream().allMatch(player -> player.didPass() || player.didBust());
    }

    public GameResults getResults() {
        Map<Integer, List<BlackjackPlayer>> scoreMap = players.parallelStream()
                .collect(Collectors.groupingBy(player -> player.handScore()));
        int winningScore = scoreMap.keySet().parallelStream()
                .reduce(0, (bestScore, score) -> {
                    if (bestScore == BLACKJACK || score > BLACKJACK || bestScore > score) {
                        return bestScore;
                    } else {
                        return score;
                    }
                });
        return new GameResults(scoreMap.getOrDefault(winningScore, Collections.EMPTY_LIST), winningScore);
    }

    public void resetGame() {
        players.addAll(waitingPlayers);
        waitingPlayers.clear();
        for (BlackjackPlayer player : players) {
            player.setReady(false);
            player.setPass(false);
            player.resetHand();
        }
        currentPlayer = 0;
        inGame = false;
    }

    public BlackjackCard getRandomCard() {
        Suit suit = Suit.values()[random.nextInt(Suit.values().length)];
        Rank rank = Rank.values()[random.nextInt(Rank.values().length)];

        return new BlackjackCard(rank, suit);
    }

    public List<BlackjackPlayer> getAllConnected() {
        List<BlackjackPlayer> allConnected = new ArrayList<>(players);
        allConnected.addAll(spectators);
        allConnected.addAll(waitingPlayers);

        return allConnected;
    }
}
