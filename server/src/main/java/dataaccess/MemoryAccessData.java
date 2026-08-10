package dataaccess;

import chess.ChessGame;
import serverFacade.ChessData.AuthData;
import serverFacade.ChessData.GameData;
import serverFacade.ChessData.UserData;
import serverFacade.RequestResponse.JoinRequest;

import java.util.Map;

public class MemoryAccessData implements AccessData {
    public void addUser(UserData newUser) {
        MemoryDatabase.addUser(newUser);
    }

    public void addAuth(AuthData newAuth) {
        MemoryDatabase.addAuth(newAuth);
    }

    public Integer addGame(GameData newGame) {
        return MemoryDatabase.addGame(newGame);
    }

    public UserData getUser(String username) {
        return MemoryDatabase.getUsers().get(username);
    }

    public AuthData getAuthData(String authToken) {return MemoryDatabase.getCurrentAuth().get(authToken);}

    public GameData getGameByID(Integer gameID) {return MemoryDatabase.getGames().get(gameID);}

    public Map<Integer, GameData> getGames() {return MemoryDatabase.getGames();}

    public void addToGame(JoinRequest request, String username) {
        GameData requestedGame = MemoryDatabase.getGames().get(request.gameID());
        switch (request.playerColor()) {
            case WHITE -> requestedGame.setWhiteUsername(username);
            case BLACK -> requestedGame.setBlackUsername(username);
        }
    }

    public void resetPlayer(Integer gameID, ChessGame.TeamColor team) {
        GameData game = getGameByID(gameID);
        if (team.equals(ChessGame.TeamColor.WHITE)) {
            game.setWhiteUsername(null);
        } else if (team.equals(ChessGame.TeamColor.BLACK)) {
            game.setBlackUsername(null);
        }
    }

    public void deleteAuth(String authToken) {
        MemoryDatabase.getCurrentAuth().remove(authToken);}

    public void clearDatabase() {
        MemoryDatabase.clear();}
}
