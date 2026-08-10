package server.Service;

import dataaccess.ConnectionException;
import dataaccess.Database;
import serverFacade.ChessData.AuthData;

public class GetAuth {
    public static AuthData getAuth(String authToken) throws ConnectionException {
        return Database.DataAccess.getAuthData(authToken);
    }
}
