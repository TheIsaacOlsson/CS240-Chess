package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import server.RequestResponse.JoinRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLWriteData implements WriteData{
    public SQLWriteData() throws DataAccessException {
        configureDatabase();
    }

    public void addUser(UserData newUser) {}
    public void addAuth(AuthData newAuth) {}
    public void addGame(GameData newGame) {}
    public void addToGame(JoinRequest request, String username) {}
    public void deleteAuth(String authToken) {}
    public void clearDatabase() {}

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = SQLDatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof ChessGame p) ps.setString(i + 1, new Gson().toJson(p));
                    else if (param == null) ps.setNull(i + 1, NULL);
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
