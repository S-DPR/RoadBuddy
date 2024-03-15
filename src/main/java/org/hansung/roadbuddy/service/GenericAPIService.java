package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;

public abstract class GenericAPIService {
    protected final ObjectMapper objectMapper;
    protected GenericAPIService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    private String sendGetHttpRequest(String endpoint, String params) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint + "?" + params))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private String queryParamMapToString(Map<String, String> params) {
        StringJoiner paramJoiner = new StringJoiner("&");
        for (Map.Entry<String, String> entry: params.entrySet()) {
            String key = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8);
            String val = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
            paramJoiner.add(key + "=" + val);
        }
        return paramJoiner.toString();
    }

    protected Map sendRequest(String endpoint, Map<String, String> params) {
        try {
            String ret = sendGetHttpRequest(endpoint, queryParamMapToString(params));
            return objectMapper.readValue(ret, Map.class);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
