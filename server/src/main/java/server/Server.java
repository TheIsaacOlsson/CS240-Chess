package server;

import dataaccess.ConnectionException;
import dataaccess.Database;
import io.javalin.*;
import server.Handlers.*;
import websocket.WebSocketHandler;

public class Server {

    private final Javalin javalin;

    public Server() {
        var webSocketHandler = new WebSocketHandler();

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
        .post("/user", RegisterHandler::tryRegister)
        .delete("/db", ClearDataHandler::clearData)
        .post("/session", LoginHandler::tryLogin)
        .delete("/session", LogoutHandler::tryLogout)
        .get("/game", GetGamesHandler::getGames)
        .post("/game", CreateGameHandler::tryCreateGame)
        .put("/game", JoinGameHandler::tryJoin)
        .ws("/ws", ws -> {
            ws.onConnect(webSocketHandler);
            ws.onMessage(webSocketHandler);
            ws.onClose(webSocketHandler);
        });
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
