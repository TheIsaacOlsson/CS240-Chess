package server.Service;

import dataaccess.DataAccessException;
import dataaccess.Database;

public class LogoutService {
    public static void logout(String authToken) throws DataAccessException {
        if ( ! ValidateService.isAuthorized(authToken)) {
            throw new DataAccessException("Error: unauthorized");
        } else {
            Database.DataAccess.deleteAuth(authToken);
        }
    }
}
