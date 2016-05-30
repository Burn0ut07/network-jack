package xyz.loejj.reddit.dailyprogrammer.server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import io.vertx.core.net.NetSocket;
import xyz.loejj.reddit.dailyprogrammer.common.cards.BlackjackCard;
import xyz.loejj.reddit.dailyprogrammer.common.protocol.MessageParser;
import xyz.loejj.reddit.dailyprogrammer.common.protocol.messages.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Server extends AbstractVerticle implements MessageHandler {
    public static final int PORT = 50000;

    private BlackjackGame blackjackGame;

    public Server() {
        this.blackjackGame = new BlackjackGame();
    }

    @Override
    public void start() throws Exception {
        NetServerOptions options = new NetServerOptions().setPort(PORT);
        NetServer server = vertx.createNetServer(options);
        server.connectHandler(this::clientConnectionHandler);
        server.listen();
    }

    private void clientConnectionHandler(NetSocket netSocket) {
        BlackjackPlayer player = new BlackjackPlayer(netSocket);
        blackjackGame.addPlayer(player);
        netSocket.closeHandler(v -> {
            System.out.printf("Client %s disconnected\n", player.getName());
            blackjackGame.removePlayer(player);
        });
        netSocket.handler(buffer -> receiveClientMessageHandler(player, buffer));
        vertx.setPeriodic(5000, id -> {
            netSocket.write(new Ping().toWireFormat());
        });
        netSocket.write(new Name(player.getName()).toWireFormat());
    }

    private void receiveClientMessageHandler(BlackjackPlayer player, Buffer buffer) {
        String[] messages = buffer.toString().split("\n");
        for (String message : messages) {
            MessageParser.getInstance().parse(message).accept(this, player);
        }
    }

    @Override
    public void handle(Name message) {
        System.out.println("Name message is no-op");
    }

    @Override
    public void handle(Ping message) {

    }

    @Override
    public void handle(Pong message, BlackjackPlayer player) {
        System.out.println("Received pong from " + player);
    }

    @Override
    public void handle(SetDisplayName message, BlackjackPlayer player) {
        System.out.printf("Changing name %s to %s\n", player.getName(), message.getDisplayName());
        player.setName(message.getDisplayName());
    }

    @Override
    public void handle(Start message, BlackjackPlayer player) {
        player.setReady(true);
        if (blackjackGame.isNewGameReady()) {
            handleGameStart();
        }
    }

    @Override
    public void handle(ChoiceQuery message) {

    }

    @Override
    public void handle(Take message, BlackjackPlayer player) {
        BlackjackCard card = blackjackGame.getRandomCard();
        player.addCardToHand(card);
        player.getNetSocket().write(new Play(card).toWireFormat());
        broadcastInfo(player, message);
    }

    @Override
    public void handle(Pass message, BlackjackPlayer player) {
        player.setPass(true);
        broadcastInfo(player, message);
        handleNextPlayer();
    }

    @Override
    public void handle(Play message, BlackjackPlayer player) {
        BlackjackCard messageCard = message.getCard();
        player.getHand().parallelStream()
                .filter(card -> card.equals(messageCard))
                .findAny()
                .ifPresent(card -> card.setHidden(false));
        broadcastInfo(player, message);
        handleNextPlayer();
    }

    @Override
    public void handle(GameOver message) {

    }

    @Override
    public void handle(Info message) {

    }

    private void handleNextPlayer() {
        Optional<BlackjackPlayer> nextPlayer = blackjackGame.getNextPlayer();
        if (nextPlayer.isPresent()) {
            nextPlayer.get().getNetSocket().write(new ChoiceQuery().toWireFormat());
        } else {
            resetIfGameOver();
        }
    }

    private void handleGameStart() {
        for (BlackjackPlayer player : blackjackGame.getPlayers()) {
            BlackjackCard card = blackjackGame.getRandomCard();
            Play firstCard = new Play(card);
            player.addCardToHand(card);
            player.getNetSocket().write(firstCard.toWireFormat());
        }

        handleNextPlayer();
    }

    private void resetIfGameOver() {
        if (blackjackGame.isGameOver()) {
            GameResults results = blackjackGame.getResults();
            List<String> winners = results.getWinners().parallelStream()
                    .map(BlackjackPlayer::getName)
                    .collect(Collectors.toList());
            blackjackGame.resetGame();
            sendToAll(blackjackGame.getAllConnected(), new GameOver(winners, results.getWinningScore()));
        }
    }

    private void sendToAll(List<BlackjackPlayer> receivers, Message message) {
        for (BlackjackPlayer receiver : receivers) {
            receiver.getNetSocket().write(message.toWireFormat());
        }
    }

    private void broadcastInfo(BlackjackPlayer doNotSendTo, Message message) {
        Info toBroadcast = new Info(message.getInfoDescription(doNotSendTo));

        for (BlackjackPlayer receiver : blackjackGame.getAllConnected()) {
            if (!receiver.equals(doNotSendTo)) {
                receiver.getNetSocket().write(toBroadcast.toWireFormat());
            }
        }
    }
}
