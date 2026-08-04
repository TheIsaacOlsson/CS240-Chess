package server;

import chess.*;
import dataaccess.DataAccessException;
import dataaccess.Database;

public class ServerMain {
    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.run(8080);

            new Database();

            System.out.println("♕ 240 Chess Server");
        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
    }
}