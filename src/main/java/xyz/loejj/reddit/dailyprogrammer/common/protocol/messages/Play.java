package xyz.loejj.reddit.dailyprogrammer.common.protocol.messages;

import xyz.loejj.reddit.dailyprogrammer.common.cards.BlackjackCard;
import xyz.loejj.reddit.dailyprogrammer.common.cards.Rank;
import xyz.loejj.reddit.dailyprogrammer.common.cards.Suit;
import xyz.loejj.reddit.dailyprogrammer.server.BlackjackPlayer;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Creation date: 2016-05-29.
 * Author: jjauregui
 * <p/>
 * Copyright 2015, Asset Science LLC. All rights reserved.
 */
public class Play implements Message {
    public static final Pattern PLAY_PATTERN =
            Pattern.compile("PLAY ([2-9]|Jack|Queen|King|Ace) of (Hearts|Diamonds|Spades|Clubs)");

    private BlackjackCard card;

    public Play(BlackjackCard card) {
        this.card = card;
    }

    @Override
    public void accept(MessageHandler handler, BlackjackPlayer player) {
        handler.handle(this, player);
    }

    public BlackjackCard getCard() {
        return card;
    }

    @Override
    public String toString() {
        return "PLAY " + card;
    }

    public static Stream<Message> parse(String message) {
        Matcher matcher = PLAY_PATTERN.matcher(message);
        if (!matcher.matches()) {
            return Stream.empty();
        }

        Optional<Rank> rankOpt = Rank.rankByAlias(matcher.group(1));
        Suit suit = Suit.valueOf(matcher.group(2));
        return rankOpt.map(rank -> Stream.<Message>of(new Play(new BlackjackCard(rank, suit))))
                .orElse(Stream.empty());
    }
}
