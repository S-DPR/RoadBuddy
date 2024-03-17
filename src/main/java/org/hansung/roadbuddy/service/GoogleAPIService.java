package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hansung.roadbuddy.dto.Coordinate;
import org.hansung.roadbuddy.enums.GoogleDirectionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleAPIService extends GenericAPIService {
    private final String apiKey;
    private final String geoCodingEndpoint = "https://maps.googleapis.com/maps/api/geocode/json";
    private final String googlePlaceEndpoint = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
    private final String googleDirectionsEndpoint = "https://maps.googleapis.com/maps/api/directions/json";
    @Autowired
    GoogleAPIService(ObjectMapper objectMapper, @Value("${api.key.google}") String apiKey) {
        super(objectMapper);
        this.apiKey = apiKey;
    }

    public Map getAddressCoordinates(String address) {
        Map<String, String> params = getParamMap();
        params.put("address", address);
        return sendRequest(geoCodingEndpoint, params);
    }

    public Map getSimilarAddressList(String address) {
        Map<String, String> params = getParamMap();
        params.put("input", address);
        params.put("type", "geocode");
        return sendRequest(googlePlaceEndpoint, params);
    }

    public Map getDirection(GoogleDirectionMode mode, Coordinate origin, Coordinate destination) {
        Map<String, String> params = getParamMap();
        params.put("origin", origin.toString());
        params.put("destination", destination.toString());
        params.put("mode", mode.name());
        return sendRequest(googleDirectionsEndpoint, params);
    }

    private Map<String, String> getParamMap() {
        Map<String, String> params = new HashMap<>();
        params.put("key", apiKey);
        return params;
    }
}
