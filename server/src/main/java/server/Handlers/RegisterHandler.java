package server.Handlers;

import com.google.gson.Gson;
import io.javalin.http.Context;
import server.RequestResponse.RegisterRequest;

public class RegisterHandler implements Handler {
    public void tryRegister(Context registration) {
        try {
            var registerReq = new Gson().fromJson(registration.body(), RegisterRequest.class);

        } catch (Error err) {
            System.out.print(err.getMessage());
        }
    }
}
