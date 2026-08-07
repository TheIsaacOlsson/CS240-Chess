package client;

import serverFacade.ResponseException;

public record EvalResult(String message, ResponseException error) {}
