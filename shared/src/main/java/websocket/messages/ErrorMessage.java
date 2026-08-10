package websocket.messages;

import serverFacade.ResponseException;

public class ErrorMessage extends ServerMessage {
    public String errorMessage;

    public ErrorMessage(ResponseException error) {
        super(ServerMessageType.ERROR);

        errorMessage = error.getMessage();
    }
}
