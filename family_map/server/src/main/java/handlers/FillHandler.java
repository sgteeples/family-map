package handlers;

import com.sun.net.httpserver.HttpHandler;

import requests.FillRequest;
import results.Result;
import services.FillService;

/** Responsible for handling requests to the Fill API */
public class FillHandler extends Handler implements HttpHandler {

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
        String exchangePathParts[] = getRequestPathAsArray(exchange);
        int usernameIndex = 2;
        int generationsIndex = 3;
        int defaultGenerations = 4;
        String username = exchangePathParts[usernameIndex];

        int gens = defaultGenerations;

        // Here we check if there are enough array elements to have a generations index
        if (exchangePathParts.length == generationsIndex + 1) {
            try {
                gens = Integer.parseInt(exchangePathParts[generationsIndex]);
            } catch(NumberFormatException e) {
                return new Result("Generations must be a nonnegative integer");
            }
        }

        return new FillService().fill(new FillRequest(username, gens));
    }
}