package handlers;

import com.sun.net.httpserver.HttpHandler;

import services.EventService;

/** Responsible for handling requests to the Event API */
public class EventHandler extends Handler implements HttpHandler {

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
        return new EventService().retrieveUserFamilyEvents(getProvidedAuthToken(exchange));
    }
}