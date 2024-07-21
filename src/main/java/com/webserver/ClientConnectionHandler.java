package com.webserver;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.webserver.Request.ClientRequestHandler;
import com.webserver.Response.ClientResponseHandler;

public class ClientConnectionHandler implements Runnable {

    private static final Logger logger = LogManager.getLogger(ServerInit.class);
    private Socket client;

    ClientConnectionHandler(Socket client) {
        logger.info("CLIENT CONNECTED");
        this.client = client;
    }

    @Override
    public void run() {
        try {
            InputStream clientInput = client.getInputStream();
            OutputStream clientOutput = client.getOutputStream();

            ClientRequestHandler request = new ClientRequestHandler(clientInput);
            ClientResponseHandler response = new ClientResponseHandler(request.getFileContent());

            response.setStatusCode(200, "Ok");
            response.setHeaders("Content-Type", "text/html");
            response.setHeaders("Content-Type", request.getFileContentType());
            response.setHeaders("server", "rohitjavaserver");

            response.send(clientOutput);

            logger.info(
                    "SERVER : [200] '" + request.getMethod() + " " + request.getPath() + "/ " + request.getProtocol()
                            + "/ " + "'");

        } catch (IOException e) {
            logger.info("SERVER ERROR :" + e);
        }
    }

}
