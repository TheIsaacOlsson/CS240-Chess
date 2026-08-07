package client;

import chess.ChessGame;
import serverFacade.ChessData.AuthData;
import serverFacade.RequestResponse.AbbrGameData;
import serverFacade.RequestResponse.CreateGameRequest;
import serverFacade.RequestResponse.JoinRequest;
import serverFacade.ResponseException;
import serverFacade.ServerFacade;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_BLACK;
import static ui.EscapeSequences.SET_TEXT_COLOR_BLUE;

public class UserClient implements Client {
    private final ServerFacade server;
    private final Scanner scanner;
    private Map<String, Object> clientData;

    public Scanner getScanner() { return scanner; }
    public String startupMessage() { return String.format("Welcome, %s!", (String) clientData.get("username")); }
    public String exitCondition() { return "Logged out"; }
    public boolean hasChildREPL() { return true; }
    public String moveToChildCondition() { return "joinGame"; }
    public void runChildREPL() {
        new ChessGameClient(server, scanner, clientData).run();

        printToUser(help());
    }

    public UserClient(ServerFacade server, Scanner scanner, Map<String, Object> clientData) {
        this.server = server;
        this.scanner = scanner;
        this.clientData = clientData;
    }

    @Override
    public EvalResult eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "join" -> joinGame(params);
                case "observe" -> observeGame(params);
                case "games" -> getGames();
                case "new" -> makeGame(params);
                case "logout" -> logout();
                default -> new EvalResult(help(), null);
            };
        } catch (ResponseException ex) {
            return new EvalResult("Error", ex);
        }
    }

    @Override
    public String help() {
        return String.format(
                """
                Type one of these commands to navigate:
                - Join a game >>> %s join <GameID> <White/Black> %s
                - Observe a game >>> %s observe <GameID> %s
                - See all games >>> %s games %s
                - Create a game >>> %s new <GameName> %s
                - See this message again >>> %s help %s
                - Logout >>> %s logout %s
                """,
                SET_TEXT_COLOR_BLACK, SET_TEXT_COLOR_BLUE,
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
            var response = server.makeGame((String) clientData.get("authToken"), new CreateGameRequest(params[0]));
            return new EvalResult(String.format("Game created with ID: %d", response.gameID()), null);
        } else {
            throw new ResponseException(400, "Expected: new <GameName>");
        }
    }

    public EvalResult joinGame(String[] params) throws ResponseException {
        // join <GameID> <White/Black>
        if (params.length >= 2) {
            String writtenID = params[0];
            int gameID;
            try {
                gameID = Integer.parseInt(writtenID);
            } catch (NumberFormatException e) {
                throw new ResponseException(400, "Invalid Game ID");
            }
            String writtenColor = params[1];
            ChessGame.TeamColor color;
            if (writtenColor.equals("white")) {
                color = ChessGame.TeamColor.WHITE;
            } else if (writtenColor.equals("black")) {
                color = ChessGame.TeamColor.BLACK;
            } else {
                throw new ResponseException(400, "Player Color must be either Black or White");
            }

            server.join((String) clientData.get("authToken"), new JoinRequest(color, gameID));
            clientData.put("gameID", gameID);
            clientData.put("playerColor", color);
            return new EvalResult("joinGame", null);
        } else {
            throw new ResponseException(400, "Expected: join <GameID> <White/Black>");
        }
    }

    public EvalResult observeGame(String[] params) throws ResponseException {
        // observe <GameID>
        if (params.length >= 1) {
            String writtenID = params[0];
            int gameID;
            try {
                gameID = Integer.parseInt(writtenID);
            } catch (NumberFormatException e) {
                throw new ResponseException(400, "Invalid Game ID");
            }

            // server.join((String) clientData.get("authToken"), new JoinRequest(color, gameID));
            clientData.put("gameID", gameID);
            clientData.put("playerColor", null);
            return new EvalResult("joinGame", null);
        } else {
            throw new ResponseException(400, "Expected: observe <GameID>");
        }
    }

    public EvalResult getGames() throws ResponseException {
        // games
        var result = server.getGames((String) clientData.get("authToken"));
        String response = listGames(result.games());
        return new EvalResult(response, null);
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
            return "To join a game, use the command \"join <GameID> <White/Black>\"";
        }
    }

    public EvalResult logout() throws ResponseException {
        // logout
        server.logout((String) clientData.get("authToken"));
        clientData.remove("authData");
        clientData.remove("username");
        return new EvalResult("Logged out", null);
    }
}