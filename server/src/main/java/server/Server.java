package server;

import io.javalin.*;
import server.Handlers.ClearDataHandler;
import server.Handlers.LoginHandler;
import server.Handlers.RegisterHandler;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
        .post("/user", RegisterHandler::tryRegister)
        .delete("/db", ClearDataHandler::clearData)
        .post("/session", LoginHandler::tryLogin);
        // Register your endpoints and exception handlers here.

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
