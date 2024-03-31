package org.hansung.roadbuddy.generic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hansung.roadbuddy.enums.HttpMethods;

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
        this.objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
    }
    private String sendGetHttpRequest(String endpoint, String params) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint + "?" + params))
                .GET()
                .build();
        System.out.println(endpoint + "?" + params);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private String sendPostHttpRequest(String endpoint, String jsonBody, GenericPostRequestDTO dto) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("Content-Type", "application/json")
                .header(dto.getApiKeyDisplayName(), dto.getApiKey())
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    private String queryParamMapToString(Map<String, String> params) {
        StringJoiner paramJoiner = new StringJoiner("&");
        for (Map.Entry<String, String> entry: params.entrySet()) {
            if (entry.getValue() == null || entry.getValue().isEmpty()) continue;
            String key = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8);
            String val = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
            paramJoiner.add(key + "=" + val);
        }
        return paramJoiner.toString();
    }

    protected Map sendRequest(String endpoint, HttpMethods methods, GenericRequestDTO dto) {
        try {
            String ret = "";
            switch (methods) {
                case GET ->
                        ret = sendGetHttpRequest(endpoint, queryParamMapToString(dto.toMap()));
                case POST ->
                        ret = sendPostHttpRequest(endpoint, objectMapper.writeValueAsString(dto.toMap()), (GenericPostRequestDTO) dto);
            }
            return objectMapper.readValue(ret, Map.class);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void setKey(GenericRequestDTO dto);
}
