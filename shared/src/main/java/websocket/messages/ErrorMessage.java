package websocket.messages;

import serverFacade.ResponseException;

public class ErrorMessage extends ServerMessage {
    public String message;

    public ErrorMessage(ServerMessageType type, ResponseException error) {
        super(type);
        assert type.equals(ServerMessageType.ERROR);

        message = error.getMessage();
    }
}
