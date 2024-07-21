package com.webserver.Request;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.webserver.ServerInit;

public class ClientRequestHandler {

    private static final Logger logger = LogManager.getLogger(ServerInit.class);

    private String base_dir = "D:\\Projects\\Java\\javawebserver\\www";
    private String method;
    private String path;
    private String protocol;
    private Map<String, String> header;
    private String body;
    private byte[] fileContent;
    private String contentType;

    public ClientRequestHandler(InputStream clientInput) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientInput));

        String line = reader.readLine();
        if (line == null || line.isEmpty()) {
            throw new IOException("Invalid HTTP request");
        }

        String[] requestLineParts = line.split(" ");
        if (requestLineParts.length < 3) {
            throw new IOException("Invalid HTTP request");
        }

        method = requestLineParts[0];
        path = requestLineParts[1];
        protocol = requestLineParts[2];
        header = new HashMap<>();

        logger.info("SERVER : " + method + "/ " + protocol + "/ " + ": Requested");

        // checking headers
        while (!(line = reader.readLine()).isEmpty()) {
            String[] headerPart = line.split(": ");
            header.put(headerPart[0], headerPart[1]);
        }
        if (header.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(header.get("Content-Length"));
            char[] buffer = new char[contentLength];
            reader.read(buffer, 0, contentLength);
            body = new String(buffer);
        }

        // check that requested path exist or not
        if (path.equals("/")) {
            path = "/index.html";
        }

        File file = new File(base_dir + path);
        // logger.info("File :" + base_dir + path);

        if (file.exists() && !file.isDirectory()) {
            fileContent = Files.readAllBytes(file.toPath());
            contentType = Files.probeContentType(file.toPath());
        } else if (file.exists()) {
            file = new File(base_dir + path + "/index.html");
            // logger.info("Directory :"+base_dir + path);
            fileContent = Files.readAllBytes(file.toPath());
            contentType = Files.probeContentType(file.toPath());
        }
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public String getFileContentType() {
        return contentType;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getProtocol() {
        return protocol;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }
}
