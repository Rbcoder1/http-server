package com.webserver.Response;

import java.util.*;
import java.io.*;

public class ClientResponseHandler {
    private String protocol;
    private int statusCode;
    private String statusMessage;
    private Map<String, String> headers;
    private byte[] fileContent;

    public ClientResponseHandler(byte[] fileContent) {
        this.protocol = "HTTP/1.1";
        this.headers = new HashMap<>();
        this.fileContent = fileContent;
    }

    public void send(OutputStream outputStream) throws IOException {
        StringBuilder response = new StringBuilder();
        response.append(protocol).append(" ").append(statusCode).append(" ").append(statusMessage).append(" ")
                .append("\r\n");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            response.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }
        response.append("\r\n");

        outputStream.write(response.toString().getBytes());
        outputStream.write(fileContent);
        outputStream.flush();
    }

    public void setStatusCode(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public void setHeaders(String key, String value) {
        headers.put(key, value);
    }
}
