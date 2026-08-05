package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import server.RequestResponse.JoinRequest;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static chess.ChessGame.TeamColor.WHITE;
import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAccessData implements AccessData {
    public SQLAccessData() throws DataAccessException {
        configureDatabase();
    }

    public void addUser(UserData newUser) {
        var statement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
        try {
            executeStatement(statement, new Object[] {newUser.username(), newUser.password(), newUser.email()}, null);
        } catch (DataAccessException e) {
            System.out.printf("Unable to add user: %s", e.getMessage());
        }
    }

    public void addAuth(AuthData newAuth) {
        var statement = "INSERT INTO authData (authToken, username) VALUES (?, ?)";
        try {
            executeStatement(statement, new Object[] {newAuth.authToken(), newAuth.username()}, null);
        } catch (DataAccessException e) {
            System.out.printf("Unable to add authorization: %s", e.getMessage());
        }
    }

    public void addGame(GameData newGame) {
        var statement = "INSERT INTO gameData (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        try {
            executeStatement(statement, new Object[] {newGame.getWhiteUsername(), newGame.getBlackUsername(), newGame.gameName, newGame.game}, null);
        } catch (DataAccessException e) {
            System.out.printf("Unable to add authorization: %s", e.getMessage());
        }
    }

    public UserData getUser(String username) {
        var statement = "SELECT password, email FROM userData WHERE username=?";
        try {
            String[] desiredData = new String[] {"password", "email"};
            ArrayList<ArrayList<Object>> queryResults = executeStatement(statement, new Object[] {username}, desiredData);
            if (queryResults == null) {
                return null;
            }
            return new UserData(username, (String) queryResults.getFirst().get(0), (String) queryResults.getFirst().get(1));
        } catch (DataAccessException e) {
            System.out.printf("User not found: %s", e.getMessage());
        }
        return null;
    }

    public AuthData getAuthData(String authToken) {
        var statement = "SELECT username FROM authData WHERE authToken=?";
        try {
            String[] desiredData = new String[] {"username"};
            ArrayList<ArrayList<Object>> queryResults = executeStatement(statement, new Object[] {authToken}, desiredData);
            if (queryResults == null) {return null;}
            return new AuthData(authToken, (String) queryResults.getFirst().getFirst());
        } catch (DataAccessException e) {
            System.out.printf("Authorization not found: %s", e.getMessage());
        }
        return null;
    }

    public GameData getGameByID(Integer gameID) {
        var statement = "SELECT * FROM gameData WHERE gameID = ?";
        try {
            String[] desiredData = new String[] {"whiteUsername", "blackUsername", "gameName", "game"};
            ArrayList<ArrayList<Object>> queryResults = executeStatement(statement, new Object[] {gameID}, desiredData);
            if (queryResults == null) {return null;}
            ArrayList<Object> flattenedResults = queryResults.getFirst();
            String game = (String) flattenedResults.get(3);
            ChessGame gameObject = new Gson().fromJson(game, ChessGame.class);
            return new GameData(gameID, (String) flattenedResults.get(0), (String) flattenedResults.get(1), (String) flattenedResults.get(2), gameObject);
        } catch (DataAccessException e) {
            System.out.printf("Authorization not found: %s", e.getMessage());
        }
        return null;
    }

    public Map<Integer, GameData> getGames() {
        var statement = "SELECT * FROM gameData";
        try {
            String[] desiredData = new String[] {"gameID", "whiteUsername", "blackUsername", "gameName", "game"};
            ArrayList<ArrayList<Object>> queryResults = executeStatement(statement, new Object[] {}, desiredData);
            Map<Integer, GameData> allGames = new HashMap<>();
            if (queryResults == null) {return allGames;}
            for (ArrayList<Object> row : queryResults) {
                String game = (String) row.get(4);
                ChessGame gameObject = new Gson().fromJson(game, ChessGame.class);
                allGames.put( (Integer) row.getFirst(), new GameData( (Integer) row.get(0), (String) row.get(1), (String) row.get(2), (String) row.get(3), gameObject));
            }
            return allGames;
        } catch (DataAccessException e) {
            System.out.printf("Authorization not found: %s", e.getMessage());
        }
        return null;
    }

    public void addToGame(JoinRequest request, String username) {
        var statement = "UPDATE gameData SET ? = ? WHERE id = ?;"; // color = username, id = request.id
        try {
            Object[] params = {request.playerColor().equals(WHITE) ? "whiteUsername" : "blackUsername", username, request.gameID()};
            executeStatement(statement, params, null);
        } catch (DataAccessException e) {
            System.out.printf("Unable to add user to game: %s", e.getMessage());
        }
    }

    public void deleteAuth(String authToken) {
        var statement = "DELETE FROM authData WHERE authToken = ?";
        try {
            executeStatement(statement, new Object[] {authToken}, null);
        } catch (DataAccessException e) {
            System.out.printf("Unable to delete authorization: %s", e.getMessage());
        }
    }

    public void clearDatabase() {
        String[] tableNames = new String[] {"gameData", "authData", "userData"};
        for (String table : tableNames) {
            try {
                executeStatement("DELETE FROM " + table, new Object[] {}, null);
            } catch (DataAccessException e) {
                System.out.printf("Unable to clear table: %s", e.getMessage());
            }
        }
        try {
            executeStatement("ALTER TABLE gameData AUTO_INCREMENT = 1", new Object[] {}, null);
        } catch (DataAccessException e) {
            System.out.printf("Unable to reset increment: %s", e.getMessage());
        }
    }

    private ArrayList<ArrayList<Object>> executeStatement(String statement, Object[] params, String[] outputColumns) throws DataAccessException {
        try (Connection conn = SQLDatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        case ChessGame p -> ps.setString(i + 1, new Gson().toJson(p));
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                if (statement.startsWith("SELECT")) {
                    ResultSet rs = ps.executeQuery();
                    if (rs == null) { return null; }
                    int i = 0;
                    ArrayList<ArrayList<Object>> output = new ArrayList<> ();
                    while (rs.next()) {
                        if (outputColumns == null) { return null; }
                        ArrayList<Object> rowData = new ArrayList<>();
                        for (int j = 0 ; j < outputColumns.length ; j++) {
                            rowData.add(rs.getObject(j+1));
                        }
                        output.add(rowData);
                        i++;
                    }
                    if (i==0) { return null; }
                    else {return output;}
                } else if (
                        statement.startsWith("INSERT")
                        || statement.startsWith("DELETE")
                        || statement.startsWith("ALTER")
                        || statement.startsWith("UPDATE")
                ) {
                    ps.executeUpdate();
                    // ResultSet keys = ps.getGeneratedKeys();
                    // return generated keys (if applicable)
                    return null;
                } else {
                    throw new DataAccessException("Cannot execute this SQL statement here");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  userData (
              `username` VARCHAR(256) NOT NULL,
              `password` TEXT NOT NULL,
              `email` TEXT NOT NULL,
              PRIMARY KEY (`username`)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS  gameData (
              `gameID` INT NOT NULL AUTO_INCREMENT,
              `whiteUsername` VARCHAR(256) DEFAULT NULL,
              `blackUsername` VARCHAR(256) DEFAULT NULL,
              `gameName` VARCHAR(256) NOT NULL,
              `game` TEXT NOT NULL,
              PRIMARY KEY (`gameID`),
              FOREIGN KEY (`whiteUsername`) REFERENCES userData(`username`),
              FOREIGN KEY (`blackUsername`) REFERENCES userData(`username`)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS  authData (
              `authToken` VARCHAR(256) NOT NULL,
              `username` VARCHAR(256) NOT NULL,
              PRIMARY KEY (`authToken`),
              FOREIGN KEY (`username`) REFERENCES userData(`username`)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        SQLDatabaseManager.createDatabase();
        try (Connection conn = SQLDatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}
