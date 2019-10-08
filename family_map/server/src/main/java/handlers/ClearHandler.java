package handlers;

import com.sun.net.httpserver.HttpHandler;

import services.ClearService;

/** Responsible for handling requests to the Clear API */
public class ClearHandler extends Handler implements HttpHandler {

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
        return new ClearService().clear();
    }
}