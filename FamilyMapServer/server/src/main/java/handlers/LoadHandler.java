package handlers;

import com.sun.net.httpserver.HttpHandler;

import json_loading.LoadData;
import requests.LoadRequest;
import results.Result;
import services.LoadService;
import utils.Deserializer;

/** Responsible for handling requests to the Load API */
public class LoadHandler extends Handler implements HttpHandler {

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
        LoadData loadData = (LoadData)d.deserialize(requestBody, LoadData.class);

        if (loadData.getUsers() == null || loadData.getPersons() == null ||
                loadData.getEvents() == null) {
            return new Result("Invalid request. Make sure request includes events, " +
                    "persons, and users arrays.");
        }

        LoadRequest req = new LoadRequest(loadData.getUsers(), loadData.getPersons(),
                loadData.getEvents());
        return new LoadService().load(req);
    }
}