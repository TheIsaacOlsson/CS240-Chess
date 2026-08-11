package websocket;

import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import serverFacade.ChessData.AuthData;
import serverFacade.ResponseException;

import jakarta.websocket.*;

import websocket.commands.LegalMoveCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.GameLoad;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//need to extend Endpoint for websocket to work properly
public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;

    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notification = switch (notification.getServerMessageType()) {
                        case NOTIFICATION -> new Gson().fromJson(message, Notification.class);
                        case ERROR -> new Gson().fromJson(message, ErrorMessage.class);
                        case LOAD_GAME -> new Gson().fromJson(message, GameLoad.class);
                    };
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException ex) {
            throw new ResponseException(500, ex.getMessage());
        } catch (IOException | URISyntaxException ex) {
            throw new ResponseException(400, ex.getMessage());
        }
    }

    //Endpoint requires this method
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, Integer gameID) throws ResponseException {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void leave(String authToken, Integer gameID) {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(400, ex.getMessage());
        }
    }

    public void resign(String authToken, Integer gameID) {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(400, ex.getMessage());
        }
    }

    public void refresh(String authToken, Integer gameID) {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.REFRESH, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void seeMoves(String authToken, Integer gameID, ChessPosition position) {
        try {
            var action = new LegalMoveCommand(authToken, gameID, position);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    public void move(String authToken, Integer gameID, ChessPosition start, ChessPosition end, ChessPiece.PieceType promotionType) {
        try {
            var action = new MakeMoveCommand(authToken, gameID, start, end, promotionType);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}