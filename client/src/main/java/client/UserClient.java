package client;

import serverFacade.ChessData.AuthData;
import serverFacade.ChessData.UserData;
import serverFacade.RequestResponse.AbbrGameData;
import serverFacade.RequestResponse.CreateGameRequest;
import serverFacade.ResponseException;
import serverFacade.ServerFacade;

import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLACK;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class UserClient implements Client {
    private final ServerFacade server;
    private final Scanner scanner;
    private final AuthData userAuth;

    public ServerFacade getServer() { return server; }
    public Scanner getScanner() { return scanner; }
    public String startupMessage() { return String.format("Welcome, %s!", userAuth.username()); }
    public String exitCondition() { return "Logged out"; }
    public boolean hasChildREPL() { return true; }
    public String moveToChildCondition() { return "joinGame"; }
    public void runChildREPL(ServerFacade server, Scanner scanner, AuthData userAuth) {
        new ChessGameClient(server, scanner, userAuth).run();

        printToUser(help());
    }

    public UserClient(ServerFacade server, Scanner scanner, AuthData auth) {
        this.server = server;
        this.scanner = scanner;
        this.userAuth = auth;
    }

    @Override
    public EvalResult eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "games" -> getGames();
                case "new" -> makeGame(params);
                case "logout" -> logout();
                default -> new EvalResult(help(), null, null);
            };
        } catch (ResponseException ex) {
            return new EvalResult("Error", null, ex);
        }
    }

    @Override
    public String help() {
        return String.format(
                """
                Type one of these commands to navigate:
                - Join a game >>> %s join <GameID> <White/Black> %s
                - See all games >>> %s games %s
                - Create a game >>> %s new <GameName> %s
                - See this message again >>> %s help %s
                - Logout >>> %s logout %s
                """,
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE,
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE,
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE,
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE,
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE
        );
    }

    public EvalResult makeGame(String[] params) throws ResponseException {
        // new <GameName>
        if (params.length >= 1) {
            var response = server.makeGame(userAuth.authToken(), new CreateGameRequest(params[0]));
            return new EvalResult(String.format("Game created with ID: %d", response.gameID()), null, null);
        } else {
            throw new ResponseException(400, "Expected: register <username> <password> <email>");
        }
    }

    public EvalResult getGames() throws ResponseException {
        // games
        var result = server.getGames(userAuth.authToken());
        String response = listGames(result.games());
        return new EvalResult(response, null, null);
    }

    private String listGames(AbbrGameData[] games) {
        if (games == null || games.length < 1) {
            return "No games available! Create your own with \"new <GameName>\".";
        } else {
            // Come back and format this as a table when working on chess board design
            printToUser("Game ID : Game Name : White Username : Black Username");
            for (AbbrGameData game : games) {
                printToUser(String.format(" %d : %s : %s : %s ", game.gameID(), game.gameName(), game.whiteUsername(), game.blackUsername()));
            }
            return "To join a game, use the command \"join <GameID>\"";
        }
    }

    public EvalResult logout() throws ResponseException {
        // logout
        server.logout(userAuth.authToken());
        return new EvalResult("Logged out", null, null);

    }
}