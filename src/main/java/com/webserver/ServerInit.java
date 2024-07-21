package com.webserver;

import com.webserver.Request.ClientRequestHandler;

import java.io.IOException;
import java.net.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerInit {

    private static final Logger logger = LogManager.getLogger(ServerInit.class);
    private int port;

    public ServerInit(int port) {
        this.port = port;
    }

    public void activate() {
        // creating a server socket connection
        try {
            ServerSocket serverInstance = new ServerSocket(port);
            logger.info("SERVER START : Listening on port " + port, port);

            // accepting connections
            while (true) {
                Socket clientConnection = serverInstance.accept();

                // Handling client request
                Thread client = new Thread(new ClientConnectionHandler(clientConnection));
                client.start();

            }

        } catch (IOException e) {
            logger.error("ERROR : ", e);
        }
    }

    public static void main(String[] args) {
        int port = 8000;
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                logger.error("ERROR : Invalid port number, Server is running on default port " + port);
            }
        }

        ServerInit server = new ServerInit(port);
        server.activate();
    }
}
