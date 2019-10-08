package edu.byu.cs240.FamilyMap.net;

import java.io.*;
import java.net.*;

import edu.byu.cs240.FamilyMap.utils.Deserializer;
import edu.byu.cs240.FamilyMap.model.LoginResult;
import edu.byu.cs240.FamilyMap.model.RegisterResult;

public class ServerProxy {

    private final String serverHost;
    private final String serverPort;

    public ServerProxy(String serverHost, String serverPort) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
    }

    public RegisterResult registerUser(String userName, String password, String email,
                                       String firstName, String lastName, String gender) {

        // The request data to be passed in when posting to server
        String reqData = "{\"userName\":\"" + userName + "\", \"password\":\"" + password +
                    "\", \"email\":\"" + email + "\", \"firstName\":\"" + firstName +
                    "\", \"lastName\":\"" + lastName + "\", \"gender\":\"" + gender + "\"}";

        return (RegisterResult)serverPost(reqData, "register", RegisterResult.class);
    }

    public LoginResult loginUser(String userName, String password) {

        // The request data to be passed in when posting to server
        String reqData = "{\"userName\":\"" + userName + "\", \"password\":\"" + password + "\"}";

        return (LoginResult)serverPost(reqData, "login", LoginResult.class);
    }

    public String getAllUserPersons(String authToken) {
        return serverGet(authToken, "person");
    }

    public String getAllUserEvents(String authToken) {
        return serverGet(authToken, "event");
    }

    private Object serverPost(String reqData, String apiName, Class resultClass) {
        Object result = null;

        try {
            HttpURLConnection http = openHttpURLConnection("user/" + apiName);
            http.setRequestMethod("POST");

            // There is a request body
            http.setDoOutput(true);

            http.connect();

            OutputStream reqBody = http.getOutputStream();

            writeString(reqData, reqBody);

            reqBody.close();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                String respData = readString(respBody);
                result = new Deserializer().deserialize(respData, resultClass);
            } else {
                System.out.println("serverPost ERROR: " + http.getResponseMessage());
            }
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    private String serverGet(String authToken, String apiName) {
        String retrieved = null;

        try {
            HttpURLConnection http = openHttpURLConnection(apiName);
            http.setRequestMethod("GET");

            // There is no request body
            http.setDoOutput(false);

            http.addRequestProperty("Authorization", authToken);

            // Connect to the server and send the HTTP request
            http.connect();

            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream respBody = http.getInputStream();
                retrieved = readString(respBody);
            } else {
                System.out.println("serverGet ERROR: " + http.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retrieved;
    }

    private HttpURLConnection openHttpURLConnection(String apiName) throws IOException {
        try {
            URL url = new URL("http://" + serverHost + ":" + serverPort + "/" + apiName);
            return (HttpURLConnection)url.openConnection();
        } catch(Exception e) {
            throw e;
        }
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }

        return sb.toString();
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}