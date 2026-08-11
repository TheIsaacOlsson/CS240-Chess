package server.Service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.ConnectionException;
import dataaccess.DataAccessException;
import dataaccess.Database;
import serverFacade.ChessData.GameData;


public class PieceMover {
    public static void movePiece(GameData gameData, ChessMove move) throws DataAccessException {
        try {
            ChessGame game = gameData.getGame();
            game.makeMove(move);
            Database.DataAccess.updateGame(gameData);
        } catch (InvalidMoveException | ConnectionException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }
}
