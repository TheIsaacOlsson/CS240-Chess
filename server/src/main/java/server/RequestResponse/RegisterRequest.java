package server.RequestResponse;

import dataaccess.UserData;

public record RegisterRequest(UserData user) {}