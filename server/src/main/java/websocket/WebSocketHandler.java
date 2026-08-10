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
import server.Service.LeaveGameService;
import serverFacade.ChessData.AuthData;
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
            if (ctx.session.isOpen() && result != null) {
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
            AuthData auth = GetAuth.getAuth(authToken);
            if (auth == null) {
                return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, new ResponseException(401, "Unauthorized"));
            }
            username = auth.username();
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
            AuthData auth = GetAuth.getAuth(authToken);
            username = auth.username();
            LeaveGameService.leaveGame(authToken, gameID);
        } catch (ConnectionException ex) {
            return new ErrorMessage(ServerMessage.ServerMessageType.ERROR, new ResponseException(500, ex.getMessage()));
        }
        connections.remove(gameID, session);
        connections.broadcast(gameID, session, new Notification(ServerMessage.ServerMessageType.NOTIFICATION, username + " has left"));
        return null;
    }

}
