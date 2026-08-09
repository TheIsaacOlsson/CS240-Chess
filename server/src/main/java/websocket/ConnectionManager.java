package websocket;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Set<Session>> gameConnections = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session) {
        gameConnections.get(gameID).add(session);
    }

    public void remove(Integer gameID, Session session) {
        gameConnections.get(gameID).remove(session);
    }

    public void broadcast(Integer gameID, Session excludeSession, ServerMessage notification) throws IOException {
        String msg = notification.toString();
        Set<Session> connections = gameConnections.get(gameID);
        for (Session c : connections) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}