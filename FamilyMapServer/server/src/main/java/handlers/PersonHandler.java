package handlers;

import com.sun.net.httpserver.HttpHandler;

import services.PersonService;

/** Responsible for handling requests to the Person API */
public class PersonHandler extends Handler implements HttpHandler {

    @Override
    boolean authTokenRequired() {
        return true;
    }

    @Override
    boolean requestMethodOK() {
        return requestMethodIsGet(exchange);
    }

    @Override
    Object getResultBodyObject(String requestBody) {
        return new PersonService().retrieveUserFamily(getProvidedAuthToken(exchange));
    }
}