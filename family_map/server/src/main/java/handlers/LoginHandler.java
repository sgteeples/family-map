package handlers;

import com.sun.net.httpserver.HttpHandler;

import requests.LoginRequest;
import services.LoginService;
import utils.Deserializer;

/** Responsible for handling requests to the Login API */
public class LoginHandler extends Handler implements HttpHandler {

    @Override
    boolean authTokenRequired() {
        return false;
    }

    @Override
    boolean requestMethodOK() {
        return requestMethodIsPost(exchange);
    }

    @Override
    Object getResultBodyObject(String requestBody) {
        Deserializer d = new Deserializer();
        LoginRequest req = (LoginRequest)d.deserialize(requestBody, LoginRequest.class);

        return new LoginService().login(req);
    }
}