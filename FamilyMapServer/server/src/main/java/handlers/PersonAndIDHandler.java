package handlers;

import com.sun.net.httpserver.HttpHandler;

import requests.PersonAndIDRequest;
import services.PersonAndIDService;

/** Responsible for handling requests to the Person API with an ID argument */
public class PersonAndIDHandler extends Handler implements HttpHandler {

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
        int personIDIndex = 2;
        String desiredPersonID = getRequestPathAsArray(exchange)[personIDIndex];

        PersonAndIDRequest req = new PersonAndIDRequest(desiredPersonID, getProvidedAuthToken(exchange));
        return new PersonAndIDService().retrievePerson(req);
    }
}
