package server.Services;

import dataaccess.DataAccessException;

public class ColorTakenException extends DataAccessException {
    public ColorTakenException(String message) {
        super(message);
    }
}
