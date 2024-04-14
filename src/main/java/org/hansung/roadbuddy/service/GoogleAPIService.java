package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hansung.roadbuddy.dto.google.request.AddressSearchReqDto;
import org.hansung.roadbuddy.dto.google.request.GoogleDirectionReqDto;
import org.hansung.roadbuddy.dto.google.request.GeocodingReqDto;
import org.hansung.roadbuddy.dto.google.response.googleDirections.GoogleDirectionResDto;
import org.hansung.roadbuddy.enums.HttpMethods;
import org.hansung.roadbuddy.generic.GenericAPIService;
import org.hansung.roadbuddy.generic.GenericRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

    public Map getAddressCoordinates(GeocodingReqDto geocodingReqDto) throws JsonProcessingException {
        setKey(geocodingReqDto);
        String response = sendRequest(geoCodingEndpoint, HttpMethods.GET, geocodingReqDto);
        return objectMapper.readValue(response, Map.class);
    }

    public Map getSimilarAddressList(AddressSearchReqDto addressSearchReqDto) throws JsonProcessingException {
        setKey(addressSearchReqDto);
        String response = sendRequest(googlePlaceEndpoint, HttpMethods.GET, addressSearchReqDto);
        return objectMapper.readValue(response, Map.class);
    }

    public GoogleDirectionResDto getDirection(GoogleDirectionReqDto directionReqDto) throws JsonProcessingException {
        setKey(directionReqDto);
        String response = sendRequest(googleDirectionsEndpoint, HttpMethods.GET, directionReqDto);
        return objectMapper.readValue(response, GoogleDirectionResDto.class);
    }

    @Override
    protected void setKey(GenericRequestDTO source) {
        source.setApiKey(apiKey);
    }
}
