package websocket;

import chess.*;
import chess.moveCalculators.*;
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
import server.Service.ValidateService;
import serverFacade.ChessData.AuthData;
import serverFacade.ChessData.GameData;
import serverFacade.ResponseException;
import websocket.commands.LegalMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.GameLoad;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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
            ServerMessage result = switch (command.getCommandType()) {
                case CONNECT -> connect(command.getGameID(), command.getAuth(), ctx.session);
                case REFRESH -> refresh(command.getGameID(), command.getAuth(), ctx.session);
                case LEGAL -> {
                    command = new Gson().fromJson(ctx.message(), LegalMoveCommand.class);
                    yield getLegal(command.getGameID(), command.getAuth(), ((LegalMoveCommand) command).getPosition(), ctx.session);
                }
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
        String team;
        try {
            AuthData auth = GetAuth.getAuth(authToken);
            if (auth == null) {
                return new ErrorMessage(new ResponseException(401, "Unauthorized"));
            }
            username = auth.username();
            GameData gameData = GetGameData.getGameByID(gameID);
            if (gameData == null) {
                return new ErrorMessage(new ResponseException(404, "Game not found"));
            }
            game = gameData.getGame();
            if (username.equals(gameData.getWhiteUsername())) {team = "White";}
            else if (username.equals(gameData.getBlackUsername())) {team = "Black";}
            else {team = "observer";}
        } catch (ConnectionException ex) {
            return new ErrorMessage(new ResponseException(500, ex.getMessage()));
        }
        connections.add(gameID, session);

        GameLoad load = new GameLoad(game);


        connections.broadcast(gameID, session, new Notification(username + " has joined as " + team));
        return load;
    }

    private ServerMessage refresh(Integer gameID, String authToken, Session session) throws IOException {
        try {
            ValidateService.isAuthorized(authToken);
            GameData gameData = GetGameData.getGameByID(gameID);
            if (gameData == null) {
                return new ErrorMessage(new ResponseException(404, "Game not found"));
            }
            ChessGame game = gameData.getGame();
            return new GameLoad(game);
        } catch (ConnectionException ex) {
            return new ErrorMessage(new ResponseException(500, ex.getMessage()));
        }
    }

    private ServerMessage leave(Integer gameID, String authToken, Session session) throws IOException {
        String username;
        try {
            AuthData auth = GetAuth.getAuth(authToken);
            username = auth.username();
            LeaveGameService.leaveGame(authToken, gameID);
        } catch (ConnectionException ex) {
            return new ErrorMessage(new ResponseException(500, ex.getMessage()));
        }
        connections.remove(gameID, session);
        connections.broadcast(gameID, session, new Notification(username + " has left"));
        return null;
    }

    private ServerMessage getLegal(Integer gameID, String authToken, ChessPosition position, Session session) {
        try {
            ValidateService.isAuthorized(authToken);
            GameData gameData = GetGameData.getGameByID(gameID);
            if (gameData == null) {
                return new ErrorMessage(new ResponseException(404, "Game not found"));
            }
            ChessGame game = gameData.getGame();
            ChessPiece piece = game.getBoard().getPiece(position);
            if (piece == null) {
                return new ErrorMessage(new ResponseException(400, "No piece found"));
            }
            Collection<ChessMove> possibleMoves = getChessMoves(position, piece, game);
            Set<ChessPosition> endSquares = new HashSet<>();
            for (ChessMove move : possibleMoves) {
                endSquares.add(move.getEndPosition());
            }
            GameLoad gameLoad = new GameLoad(game);
            gameLoad.highlightSquares = endSquares;
            return gameLoad;
        } catch (ConnectionException ex) {
            return new ErrorMessage(new ResponseException(500, ex.getMessage()));
        }
    }

    private static Collection<ChessMove> getChessMoves(ChessPosition position, ChessPiece piece, ChessGame game) {
        ChessPiece.PieceType type = piece.getPieceType();
        Collection<ChessMove> possibleMoves = switch (type) {
            case KING -> new KingMoveCalculator(game.getBoard(), position).getMoves();
            case QUEEN -> new QueenMoveCalculator(game.getBoard(), position).getMoves();
            case BISHOP -> new BishopMoveCalculator(game.getBoard(), position).getMoves();
            case KNIGHT -> new KnightMoveCalculator(game.getBoard(), position).getMoves();
            case ROOK -> new RookMoveCalculator(game.getBoard(), position).getMoves();
            case PAWN -> new PawnMoveCalculator(game.getBoard(), position).getMoves();
        };
        return possibleMoves;
    }
}
