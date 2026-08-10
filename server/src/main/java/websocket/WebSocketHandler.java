package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;

import org.eclipse.jetty.websocket.api.Session;

import org.jetbrains.annotations.NotNull;
import websocket.commands.UserGameCommand;
import websocket.messages.GameLoad;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;


import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> connect(command.getGameID(), ctx.session);
                case LEAVE -> leave(command.getGameID(), ctx.session);
                // case EXIT -> exit(command.visitorName(), ctx.session);
                default -> throw new IOException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) {
        System.out.println("Disconnected");
    }

    public void connect(Integer gameID, Session session) throws IOException {
        String username = "null"; // get username from authData
        connections.add(gameID, session);
        ChessGame game = new ChessGame(); //get game data
        GameLoad load = new GameLoad(ServerMessage.ServerMessageType.LOAD_GAME, game);
        if (session.isOpen()) {
            session.getRemote().sendString(new Gson().toJson(load));
        }
        connections.broadcast(gameID, session, new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " is now observing"));
    }

    private void leave(Integer gameID, Session session) throws IOException {
        String username = "null";
        connections.remove(gameID, session);
        connections.broadcast(gameID, session, new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has left"));
    }

    /*
    private void enter(String visitorName, Session session) throws IOException {
        connections.add(session);
        var message = String.format("%s is in the shop", visitorName);
        var notification = new ServerMessage(ServerMessage.Type.ARRIVAL, message);
        connections.broadcast(session, notification);
    }

    private void exit(String visitorName, Session session) throws IOException {
        var message = String.format("%s left the shop", visitorName);
        var notification = new ServerMessage(ServerMessage.Type.DEPARTURE, message);
        connections.broadcast(session, notification);
        connections.remove(session);
    }

    public void makeNoise(String petName, String sound) throws ResponseException {
        try {
            var message = String.format("%s says %s", petName, sound);
            var notification = new ServerMessage(ServerMessage.Type.NOISE, message);
            connections.broadcast(null, notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
     */
}
