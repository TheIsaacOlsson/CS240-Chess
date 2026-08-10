package websocket.messages;

import serverFacade.ResponseException;

public class ErrorMessage extends ServerMessage {
    public String errorMessage;

    public ErrorMessage(ServerMessageType type, ResponseException error) {
        super(type);
        assert type.equals(ServerMessageType.ERROR);

        errorMessage = error.getMessage();
    }
}
