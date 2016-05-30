package xyz.loejj.reddit.dailyprogrammer.common.protocol;

import xyz.loejj.reddit.dailyprogrammer.common.protocol.messages.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Creation date: 2016-05-28.
 * Author: jjauregui
 * <p/>
 * Copyright 2015, Asset Science LLC. All rights reserved.
 */
public class MessageParser {
    private List<Function<String, Stream<Message>>> parserFunctions;

    private MessageParser() {
        this.parserFunctions = new ArrayList<>();
        parserFunctions.add(Ping::parse);
        parserFunctions.add(Pong::parse);
        parserFunctions.add(SetDisplayName::parse);
        parserFunctions.add(Name::parse);
        parserFunctions.add(Start::parse);
        parserFunctions.add(ChoiceQuery::parse);
        parserFunctions.add(Take::parse);
        parserFunctions.add(Pass::parse);
        parserFunctions.add(Play::parse);
        parserFunctions.add(GameOver::parse);
        parserFunctions.add(Info::parse);
    }

    public Message parse(String message) {
        return parserFunctions.stream().flatMap(parser -> parser.apply(message))
                .findFirst().orElse(new UnparseableMessage(message));
    }

    public static MessageParser getInstance() {
        return MessageParserHolder.instance;
    }

    private static class MessageParserHolder {
        public static MessageParser instance = new MessageParser();
    }
}
