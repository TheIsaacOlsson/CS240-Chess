package serverFacade;

import com.google.gson.Gson;
import serverFacade.ChessData.UserData;
import serverFacade.RequestResponse.*;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    // Register
    public RegisterResponse register(UserData userData) throws ResponseException {
        var request = buildRequest("POST", "/user", userData);
        var response = sendRequest(request);
        return handleResponse(response, RegisterResponse.class);
    }

    // Login
    public LoginResponse login(LoginRequest login) throws ResponseException {
        var request = buildRequest("POST", "/session", login);
        var response = sendRequest(request);
        return handleResponse(response, LoginResponse.class);
    }

    // Logout
    public void logout(String authorization) throws ResponseException {
        var request = buildRequest("DELETE", "/session", null, authorization);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    // getGames
    public GetGamesResponse getGames(String authorization) throws ResponseException {
        var request = buildRequest("GET", "/game", null, authorization);
        var response = sendRequest(request);
        return handleResponse(response, GetGamesResponse.class);
    }

    // createGame
    public CreateGameResponse makeGame(String authorization, CreateGameRequest gameReq) throws ResponseException {
        var request = buildRequest("POST", "/game", gameReq, authorization);
        var response = sendRequest(request);
        return handleResponse(response, CreateGameResponse.class);
    }

    // joinGame
    public void join(String authorization, JoinRequest joinReq) throws ResponseException {
        var request = buildRequest("PUT", "/game", joinReq, authorization);
        var response = sendRequest(request);
        handleResponse(response, null);
    }

    /*
    private void clearDatabase() throws ResponseException {
        var request = buildRequest("DELETE", "/db", null);
        sendRequest(request);
    }
    */

    private HttpRequest buildRequest(String method, String path, Object body, String authorization) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (authorization != null) {
            request.setHeader("authorization", authorization);
        }
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        return buildRequest(method, path, body, null);
    }


    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            var responseObject = new Gson().fromJson(body, ErrorResponse.class);
            throw new ResponseException(status, responseObject.message());
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}