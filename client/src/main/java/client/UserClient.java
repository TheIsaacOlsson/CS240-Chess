package client;

import serverFacade.ChessData.AuthData;
import serverFacade.ResponseException;
import serverFacade.ServerFacade;

import java.util.Scanner;

public class UserClient implements Client {
    private final ServerFacade server;
    private final Scanner scanner;
    private final AuthData userAuth;

    public ServerFacade getServer() { return server; }
    public String startupMessage() { return String.format("Welcome, %s!", userAuth.username()); }
    public String exitCondition() { return "logout"; }
    public boolean hasChildREPL() { return true; }
    public String moveToChildCondition() { return "joinGame"; }
    public void runChildREPL(ServerFacade server, Scanner scanner, AuthData userAuth) {
        new ChessGameClient(server, scanner, userAuth).run();
    }

    public UserClient(ServerFacade server, Scanner scanner, AuthData auth) {
        this.server = server;
        this.scanner = scanner;
        this.userAuth = auth;
    }

    @Override
    public EvalResult eval(String input) {
        return new EvalResult("", null, null);
    }

    @Override
    public String help() {
        return "A string that shows what the possible commands are (with displayed input format)";
    }
}