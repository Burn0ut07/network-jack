package xyz.loejj.reddit.dailyprogrammer.client;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetClientOptions;
import io.vertx.core.net.NetSocket;
import xyz.loejj.reddit.dailyprogrammer.common.cards.BlackjackCard;
import xyz.loejj.reddit.dailyprogrammer.common.protocol.MessageParser;
import xyz.loejj.reddit.dailyprogrammer.common.protocol.messages.*;
import xyz.loejj.reddit.dailyprogrammer.server.BlackjackPlayer;
import xyz.loejj.reddit.dailyprogrammer.server.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Creation date: 2016-05-28.
 * Author: jjauregui
 * <p/>
 * Copyright 2015, Asset Science LLC. All rights reserved.
 */
public class Client extends AbstractVerticle implements MessageHandler {
    private List<BlackjackCard> hand;
    private NetSocket netSocket;
    private BufferedReader console;

    public Client() {
        this.hand = new ArrayList<>();
        this.console = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void start() throws Exception {
        System.out.println("Connecting to server...");
        NetClientOptions options = new NetClientOptions().setConnectTimeout(10000);
        NetClient client = vertx.createNetClient(options);
        client.connect(Server.PORT, "localhost", res -> {
            if (res.succeeded()) {
                netSocket = res.result();
                netSocket.handler(this::receiveServerMessageHandler);
            }
        });
    }

    private void receiveServerMessageHandler(Buffer buffer) {
        String[] messages = buffer.toString().split("\n");
        for (String message : messages) {
            MessageParser.getInstance().parse(message).accept(this, null);
        }
    }

    @Override
    public void handle(Ping message) {
        netSocket.write(new Pong().toWireFormat());
    }

    @Override
    public void handle(Pong message, BlackjackPlayer player) {

    }

    @Override
    public void handle(Name message) {
        vertx.executeBlocking(future -> {
            System.out.println("Logged in!");
            String username = "";
            while (username.isEmpty()) {
                System.out.print("Username (No spaces): ");
                try {
                    username = console.readLine().replaceAll("\\s", "");
                } catch (IOException e) {
                    System.err.println("Could not read username.");
                    e.printStackTrace();
                }
            }
            future.complete(username);
        }, res -> {
            SetDisplayName setDisplayName = new SetDisplayName(message.getName(), res.result().toString());
            netSocket.write(setDisplayName.toWireFormat());
            handleRoundStart();
        });
    }

    @Override
    public void handle(SetDisplayName message, BlackjackPlayer player) {

    }

    @Override
    public void handle(Start message, BlackjackPlayer player) {

    }

    @Override
    public void handle(ChoiceQuery message) {
        vertx.executeBlocking(future -> {
            String playChoice = "0";
            while (isInvalidChoice(playChoice)) {
                System.out.println("Choose play");
                System.out.println("1. Take");
                System.out.println("2. Pass");
                try {
                    playChoice = console.readLine();
                } catch (IOException e) {
                    System.err.println("Could not read play choice.");
                    e.printStackTrace();
                }
            }
            future.complete(playChoice);
        }, res -> {
            String choice = res.result().toString();
            Message responseMessage;
            if (choice.equals("1")) {
                responseMessage = new Take();
            } else {
                responseMessage = new Pass();
            }
            netSocket.write(responseMessage.toWireFormat());
        });
    }

    @Override
    public void handle(Take message, BlackjackPlayer player) {

    }

    @Override
    public void handle(Pass message, BlackjackPlayer player) {

    }

    @Override
    public void handle(Play message, BlackjackPlayer player) {
        hand.add(message.getCard());
        System.out.println("Received: " + message.getCard());
        if (hand.size() > 1) {
            vertx.executeBlocking(future -> {
                BlackjackCard hiddenCard = hand.parallelStream()
                        .filter(card -> card.isHidden() && !card.equals(message.getCard()))
                        .findAny()
                        .orElseThrow(() -> new RuntimeException("No hidden card in hand!"));
                String cardToPlay = "0";
                while (isInvalidChoice(cardToPlay)) {
                    System.out.println("Choose card to play");
                    System.out.println("1. " + hiddenCard);
                    System.out.println("2. " + message.getCard());
                    try {
                        cardToPlay = console.readLine();
                    } catch (IOException e) {
                        System.err.println("Could not read card choice.");
                        e.printStackTrace();
                    }
                }
                if (cardToPlay.equals("1")) {
                    future.complete(hiddenCard);
                } else {
                    future.complete(message.getCard());
                }
            }, res -> {
                BlackjackCard card = (BlackjackCard) res.result();
                card.setHidden(false);
                netSocket.write(new Play(card).toWireFormat());
            });
        }
    }

    @Override
    public void handle(GameOver message) {
        hand.clear();
        System.out.println("Round is over!");
        if (message.getWinningScore() > 0) {
            System.out.printf("Congratulations to winners with score %d!\n", message.getWinningScore());
            for (String winner : message.getWinners()) {
                System.out.println(winner);
            }
        } else {
            System.out.println("Everyone busted! No winners this round.");
        }
        System.out.println("New round starting!");
        handleRoundStart();
    }

    public void handleRoundStart() {
        vertx.executeBlocking(future -> {
            String choice = "0";
            while (isInvalidChoice(choice)) {
                System.out.println("Choose to join round or leave.");
                System.out.println("1. Join");
                System.out.println("2. Leave");
                try {
                    choice = console.readLine();
                } catch (IOException e) {
                    System.err.println("Could not read round choice.");
                    e.printStackTrace();
                }
            }

            future.complete(choice);
        }, res -> {
            String choice = res.result().toString();
            if (choice.equals("1")) {
                System.out.println("Waiting for other players.");
                netSocket.write(new Start().toWireFormat());
            } else {
                vertx.close();
            }
        });
    }

    private boolean isInvalidChoice(String choice) {
        return !(choice.equals("1") || choice.equals("2"));
    }
}
