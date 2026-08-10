package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ErrorMessage;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Set<Session>> gameConnections = new ConcurrentHashMap<>();

    public void add(Integer gameID, Session session) {
        if (gameConnections.containsKey(gameID)) {
            gameConnections.get(gameID).add(session);
        } else {
            Set<Session> newSession = new HashSet<>();
            newSession.add(session);
            gameConnections.put(gameID, newSession);
        }
    }

    public void remove(Integer gameID, Session session) {
        gameConnections.get(gameID).remove(session);
    }

    public void broadcast(Integer gameID, Session excludeSession, ServerMessage notification) throws IOException {
        String msg = switch (notification.getServerMessageType()) {
            case NOTIFICATION -> new Gson().toJson((Notification) notification);
            case ERROR -> new Gson().toJson((ErrorMessage) notification);
            case LOAD_GAME -> null;
        };
        if (msg == null) {
            return;
        }
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