package service;

import dataaccess.AuthData;
import dataaccess.Database;
import dataaccess.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import server.Services.RegisterService;

public class RegisterTests {
    @Test
    @Order(1)
    @DisplayName("Registration Success")
    public void registrationSuccess() {
        UserData newUser = new UserData("my_username", "my_password", "my_email@email.git");
        AuthData newUserAuth = RegisterService.register(newUser);
        Assertions.assertFalse(Database.getUsers().isEmpty(), "Database is empty");
        Assertions.assertTrue(Database.getUsers().containsKey("my_username"), "Username not found");

        Assertions.assertNotNull(newUserAuth);
        Assertions.assertFalse(Database.getCurrentAuth().isEmpty(), "No AuthData saved");
        Assertions.assertTrue(Database.getCurrentAuth().containsKey(newUserAuth.authToken()));
    }

    @Test
    @Order(2)
    @DisplayName("Username Taken")
    public void usernameTakenFailure() {
        UserData userOne = new UserData("my_username", "my_password", "my_email@email.git");
        RegisterService.register(userOne);

        UserData userTwo = new UserData("my_username", "new_pass", "different@email.yahoo");
        AuthData secondUserAuth = RegisterService.register(userTwo);

        Assertions.assertNull(secondUserAuth);
        Assertions.assertTrue(Database.getUsers().containsValue(userOne));
    }
}
