package com.Equipo07_SportPulseMS.ms_gateway.util;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import java.io.IOException;

public class MockServerManager {

    private MockWebServer server;

    public void start() throws IOException {
        server = new MockWebServer();
        server.start();
    }

    public void shutdown() throws IOException {
        if (server != null) {
            server.shutdown();
        }
    }

    public String getBaseUrl() {
        return server.url("/").toString();
    }

    // 🔥 genérico (para routing, auth, login, etc.)
    public void enqueueResponse(String body) {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(body)
                .addHeader("Content-Type", "application/json"));
    }

    // 🔥 health UP
    public void enqueueHealthUp() {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"status\":\"UP\"}")
                .addHeader("Content-Type", "application/json"));
    }

    // 🔥 health DOWN
    public void enqueueHealthDown() {
        server.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("{\"status\":\"DOWN\"}"));
    }

    // respuesta OK genérica (UP)
    public void enqueueUp() {
        server.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("{\"status\":\"UP\"}")
                .addHeader("Content-Type", "application/json"));
    }

    // respuesta DOWN
    public void enqueueDown() {
        server.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("{\"status\":\"DOWN\"}"));
    }
}
