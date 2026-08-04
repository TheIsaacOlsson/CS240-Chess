package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import server.RequestResponse.JoinRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLAccessData implements AccessData {
    public SQLAccessData() throws DataAccessException {
        configureDatabase();
    }

    public void addUser(UserData newUser) {
        var statement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
        try {
            executeUpdate(statement, newUser.username(), newUser.password(), newUser.email());
        } catch (DataAccessException e) {
            System.out.printf("Unable to add user: %s", e.getMessage());
        }
    }

    public void addAuth(AuthData newAuth) {
        var statement = "INSERT INTO authData (authToken, username) VALUES (?, ?)";
        try {
            executeUpdate(statement, newAuth.authToken(), newAuth.username());
        } catch (DataAccessException e) {
            System.out.printf("Unable to add authorization: %s", e.getMessage());
        }
    }

    public void addGame(GameData newGame) {
        var statement = "INSERT INTO gameData (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        try {
            executeUpdate(statement, newGame.getWhiteUsername(), newGame.getBlackUsername(), newGame.gameName, newGame.game);
        } catch (DataAccessException e) {
            System.out.printf("Unable to add authorization: %s", e.getMessage());
        }
    }

    public UserData getUser(String username) {
        return null;
    }

    public AuthData getAuthData(String authToken) {return null;}

    public Map<Integer, GameData> getGames() {return null;}

    public void addToGame(JoinRequest request, String username) {}

    public void deleteAuth(String authToken) {}

    public void clearDatabase() {}

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
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
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
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
