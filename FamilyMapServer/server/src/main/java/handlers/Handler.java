package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.sql.Connection;

import database_access.Database;
import database_access.DatabaseException;
import results.Result;
import utils.Serializer;

/** Abstraction of a handler */
abstract class Handler implements HttpHandler {

    HttpExchange exchange;

    /** Handles an HTTP request
     *
     * @param exchange A HttpExchange object representing the HTTP exchange
     * @throws IOException Thrown if there is an exception with reading/writing
     */
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        this.exchange = exchange;
        Serializer s = new Serializer();
        String returnString = "";
        Object result;

        try {
            // Check if we have the right GET / POST value in the request
            if (requestMethodOK()) {
                // If an authorization token is needed, confirm that it's valid
                if (authTokenRequired() && !authTokenWasProvided(exchange)) {
                    result = new Result("No auth token was provided");
                } else if (authTokenRequired() && !authTokenExists(exchange)) {
                    result = new Result("Provided auth token is not registered");
                } else {
                    InputStream reqBody = exchange.getRequestBody();
                    String requestBody = readString(reqBody);
                    result = getResultBodyObject(requestBody);
                }

                // We get the object we want to send back as JSON
                // and notify the user that everything went OK
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                returnString = s.serialize(result);
            } else {
                // If we didn't have the correct get / post value
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                result = new Result("Incorrect GET / POST value");
                returnString = s.serialize(result);
            }
        } catch (Exception e) {
            // The error was on our end
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            result = new Result(e.getMessage());
            returnString = s.serialize(result);
        } finally {
            writeStringToResponseBody(returnString, exchange);
            exchange.getResponseBody().close();
        }
    }

    private boolean authTokenExists(HttpExchange exchange) {
        boolean exists = false;
        Database db = new Database();
        try {
            Connection conn = db.openConnection();
            String token = getProvidedAuthToken(exchange);
            exists = !db.getAuthTokenDao().find("authToken", token).isEmpty();
        } catch (DatabaseException e) {
            System.out.println(e.getMessage());
        } finally {
            db.closeConnection(false);
        }
        return exists;
    }

    String getProvidedAuthToken(HttpExchange exchange) {
        return exchange.getRequestHeaders().getFirst("Authorization");
    }

    private boolean authTokenWasProvided(HttpExchange exchange) {
        return exchange.getRequestHeaders().containsKey("Authorization");
    }

    String[] getRequestPathAsArray(HttpExchange exchange) {
        return exchange.getRequestURI().getPath().split("/");
    }

    boolean requestMethodIsGet(HttpExchange exchange) {
        return exchange.getRequestMethod().toLowerCase().equals("get");
    }

    boolean requestMethodIsPost(HttpExchange exchange) {
        return exchange.getRequestMethod().toLowerCase().equals("post");
    }

    private void writeStringToResponseBody(String str, HttpExchange exchange) throws IOException {
        OutputStream os = exchange.getResponseBody();
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    abstract boolean requestMethodOK();
    abstract Object getResultBodyObject(String requestBody);
    abstract boolean authTokenRequired();
}