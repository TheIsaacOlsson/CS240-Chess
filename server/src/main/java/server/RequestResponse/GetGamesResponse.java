package server.RequestResponse;

import dataaccess.GameData;

public record GetGamesResponse(AbbrGameData[] games, String message) {}
