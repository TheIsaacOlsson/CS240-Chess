package dataaccess;

public class ConnectionException extends DataAccessException {
    public ConnectionException(String message) {
        super(message);
    }
    public ConnectionException(String message, Throwable ex) {
        super(message, ex);
    }
}
