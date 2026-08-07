package client;

import serverFacade.ChessData.AuthData;

public record EvalResult(String message, AuthData auth, Throwable error) {}
