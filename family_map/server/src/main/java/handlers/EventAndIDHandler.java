package handlers;

import com.sun.net.httpserver.HttpHandler;

import requests.EventAndIDRequest;
import services.EventAndIDService;

/** Responsible for handling requests to the Event API with an ID argument */
public class EventAndIDHandler extends Handler implements HttpHandler {

    @Override
    boolean authTokenRequired() {
        return true;
    }

    @Override
    boolean requestMethodOK() {
        return requestMethodIsGet(exchange);
    }

    Object getResultBodyObject(String requestBody) {
        int eventIDIndex = 2;
        String desiredEventID = getRequestPathAsArray(exchange)[eventIDIndex];
        EventAndIDRequest req = new EventAndIDRequest(desiredEventID, getProvidedAuthToken(exchange));

        return new EventAndIDService().retrieveEvent(req);
    }
}