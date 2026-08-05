package server;

import dataaccess.ConnectionException;
import dataaccess.Database;
import io.javalin.*;
import server.Handlers.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
        .post("/user", RegisterHandler::tryRegister)
        .delete("/db", ClearDataHandler::clearData)
        .post("/session", LoginHandler::tryLogin)
        .delete("/session", LogoutHandler::tryLogout)
        .get("/game", GetGamesHandler::getGames)
        .post("/game", CreateGameHandler::tryCreateGame)
        .put("/game", JoinGameHandler::tryJoin);
        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);

        try {
            new Database();
        } catch (ConnectionException e) {
            throw new RuntimeException(String.format("Database configuration failed: %s", e.getMessage()));
        }

        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
