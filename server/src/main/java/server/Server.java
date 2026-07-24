package server;

import io.javalin.*;
import server.Handlers.ClearDataHandler;
import server.Handlers.RegisterHandler;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
        .post("/user", context -> new RegisterHandler().tryRegister(context))
        .delete("/db", context -> new ClearDataHandler().clearData(context));
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
