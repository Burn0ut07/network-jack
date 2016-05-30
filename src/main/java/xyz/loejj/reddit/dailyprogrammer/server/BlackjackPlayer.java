package xyz.loejj.reddit.dailyprogrammer.server;

import io.vertx.core.net.NetSocket;
import xyz.loejj.reddit.dailyprogrammer.common.cards.BlackjackCard;
import xyz.loejj.reddit.dailyprogrammer.common.cards.Rank;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Creation date: 2016-05-28.
 * Author: jjauregui
 * <p/>
 * Copyright 2015, Asset Science LLC. All rights reserved.
 */
public class BlackjackPlayer {
    private NetSocket netSocket;
    private String id;
    private String name;
    private boolean ready;
    private List<BlackjackCard> hand;
    private boolean pass;

    public BlackjackPlayer(NetSocket netSocket, String id) {
        this.netSocket = netSocket;
        this.id = id;
        this.name = id;
        this.ready = false;
        this.pass = false;
        this.hand = new ArrayList<>();
    }

    public BlackjackPlayer(NetSocket netSocket) {
        this(netSocket, UUID.randomUUID().toString());
    }

    public NetSocket getNetSocket() {
        return netSocket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public void addCardToHand(BlackjackCard card) {
        hand.add(card);
    }

    public void resetHand() {
        hand.clear();
    }

    public List<BlackjackCard> getHand() {
        return hand;
    }

    public boolean didPass() {
        return pass;
    }

    public void setPass(boolean pass) {
        this.pass = pass;
    }

    public int handScore() {
        Map<Boolean, List<BlackjackCard>> cardByAce = hand.parallelStream()
                .collect(Collectors.groupingBy(card -> card.getRank().equals(Rank.Ace)));
        int nonAceSum = cardByAce.getOrDefault(false, Collections.emptyList()).parallelStream()
                .mapToInt(card -> card.getRank().getValue())
                .sum();
        int handScore = cardByAce.getOrDefault(true, Collections.emptyList()).stream()
                .reduce(nonAceSum, (sum, card) -> {
                    if (sum + card.getRank().getValue() > BlackjackGame.BLACKJACK) {
                        return sum + 1;
                    } else {
                        return sum + card.getRank().getValue();
                    }
                }, Integer::sum);

        return handScore;
    }

    public boolean didBust() {
        return handScore() > BlackjackGame.BLACKJACK;
    }

    @Override
    public String toString() {
        return "BlackjackPlayer{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
