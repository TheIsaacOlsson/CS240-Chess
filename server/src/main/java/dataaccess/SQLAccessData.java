package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import org.eclipse.jetty.server.Authentication;
import server.RequestResponse.JoinRequest;

import java.sql.*;
import java.util.ArrayList;
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
            return new AuthData(authToken, (String) queryResults.getFirst().getFirst());
        } catch (DataAccessException e) {
            System.out.printf("Authorization not found: %s", e.getMessage());
        }
        return null;
    }

    public Map<Integer, GameData> getGames() {return null;}

    public void addToGame(JoinRequest request, String username) {}

    public void deleteAuth(String authToken) {}

    public void clearDatabase() {}

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
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                int i = 0;
                ArrayList<ArrayList<Object>> output = new ArrayList<> ();
                while (rs.next()) {
                    if (outputColumns == null) { return arrayWrap(1); }
                    ArrayList<Object> rowData = new ArrayList<>();
                    for (int j = 0 ; j < outputColumns.length ; j++) {
                        rowData.add(rs.getObject(j+1));
                    }
                    output.add(rowData);
                    i++;
                }
                if (i==0) { return arrayWrap(0); }
                else {return output;}
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private ArrayList<ArrayList<Object>> arrayWrap(Object input) {
        ArrayList<ArrayList<Object>> outer = new ArrayList<>();
        ArrayList<Object> inner = new ArrayList<>();
        inner.add(input);
        outer.add(inner);
        return outer;
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
