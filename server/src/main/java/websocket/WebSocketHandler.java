package websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.ConnectionException;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;

import org.eclipse.jetty.websocket.api.Session;

import org.jetbrains.annotations.NotNull;
import server.Service.GetAuth;
import server.Service.GetGameData;
import serverFacade.ChessData.GameData;
import serverFacade.ResponseException;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.GameLoad;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;


import java.io.IOException;

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
            ServerMessage result = switch (command.getCommandType()) {
                case CONNECT -> connect(command.getGameID(), command.getAuth(), ctx.session);
                case LEAVE -> leave(command.getGameID(), command.getAuth(), ctx.session);
                default -> throw new IOException();
            };
            if (ctx.session.isOpen()) {
                ctx.session.getRemote().sendString(new Gson().toJson(result));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(@NotNull WsCloseContext ctx) {
        System.out.println("Disconnected");
    }

    public ServerMessage connect(Integer gameID, String authToken, Session session) throws IOException {
        String username;
        ChessGame game;
        try {
            username = GetAuth.getAuth(authToken).username();
            GameData gameData = GetGameData.getGameByID(gameID);
            if (gameData == null) {
                return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, new ResponseException(404, "Game not found"));
            }
            game = gameData.getGame();
        } catch (ConnectionException ex) {
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, new ResponseException(500, ex.getMessage()));
        }
        connections.add(gameID, session);

        GameLoad load = new GameLoad(ServerMessage.ServerMessageType.LOAD_GAME, game);
        connections.broadcast(gameID, session, new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " is now observing"));
        return load;
    }

    private ServerMessage leave(Integer gameID, String authToken, Session session) throws IOException {
        String username;
        try {
            username = GetAuth.getAuth(authToken).username();
        } catch (ConnectionException ex) {
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, new ResponseException(500, ex.getMessage()));
        }
        connections.remove(gameID, session);
        connections.broadcast(gameID, session, new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has left"));
        return new Notification(ServerMessage.ServerMessageType.NOTIFICATION, "You left");
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
