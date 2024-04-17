package org.hansung.roadbuddy.generic;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
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
    private final String apiKey;
    protected final ObjectMapper objectMapper;
    protected volatile XmlMapper xmlMapper;
    protected GenericAPIService(ObjectMapper objectMapper, String apiKey) {
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
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

    protected String sendRequest(String endpoint, HttpMethods methods, GenericRequestDTO dto) {
        try {
            switch (methods) {
                case GET -> {
                    return sendGetHttpRequest(endpoint, queryParamMapToString(dto.toMap()));
                }
                case POST -> {
                    return sendPostHttpRequest(
                            endpoint,
                            objectMapper.writeValueAsString(dto.toMap()),
                            (GenericPostRequestDTO) dto);
                }
                default -> {
                    return null;
                }
            }
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void setKey(GenericRequestDTO dto) {
        dto.setApiKey(apiKey);
    };
}
