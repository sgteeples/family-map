package utils;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import handlers.ClearHandler;
import handlers.DefaultHandler;
import handlers.EventAndIDHandler;
import handlers.EventHandler;
import handlers.FillHandler;
import handlers.LoadHandler;
import handlers.LoginHandler;
import handlers.PersonAndIDHandler;
import handlers.PersonHandler;
import handlers.RegisterHandler;

/** Responsible for running the server, and listening for requests */
class Server {

    private static final int maxWaitingConnections = 12;

    private void run(String portNumber) {
        System.out.println("Initializing HTTP Server");

        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(Integer.parseInt(portNumber)),
                                       maxWaitingConnections);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }

        server.setExecutor(null);

        System.out.println("Creating contexts");
        server.createContext("/", new DefaultHandler());
        server.createContext("/clear", new ClearHandler());
        server.createContext("/event", new EventHandler());
        server.createContext("/event/", new EventAndIDHandler());
        server.createContext("/fill", new FillHandler());
        server.createContext("/load", new LoadHandler());
        server.createContext("/person", new PersonHandler());
        server.createContext("/person/", new PersonAndIDHandler());
        server.createContext("/user/login", new LoginHandler());
        server.createContext("/user/register", new RegisterHandler());

        System.out.println("Starting server");
        server.start();

        System.out.println("Server started");
    }

    /** Takes in a port number, and starts the server at that port
     *
     * @param args Command line arguments, including a port number to run the server at
     */
    public static void main(String[] args) {
        String portNumber = args[0];
        new Server().run(portNumber);
    }
}
