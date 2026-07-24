package server.RequestResponse;

import dataaccess.AuthData;

public record RegisterResponse (String username, String authToken, String message) {}
