package client;

import serverFacade.ChessData.AuthData;

public record EvalResult(String message, Throwable error) {}
