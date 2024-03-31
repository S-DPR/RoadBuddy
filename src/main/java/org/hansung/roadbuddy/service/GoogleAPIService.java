package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hansung.roadbuddy.dto.google.AddressSearchReqDto;
import org.hansung.roadbuddy.dto.google.GoogleDirectionReqDto;
import org.hansung.roadbuddy.dto.google.GeocodingReqDto;
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

    public Map getAddressCoordinates(GeocodingReqDto geocodingReqDto) {
        setKey(geocodingReqDto);
        return sendRequest(geoCodingEndpoint, HttpMethods.GET, geocodingReqDto);
    }

    public Map getSimilarAddressList(AddressSearchReqDto addressSearchReqDto) {
        setKey(addressSearchReqDto);
        return sendRequest(googlePlaceEndpoint, HttpMethods.GET, addressSearchReqDto);
    }

    public Map getDirection(GoogleDirectionReqDto directionReqDto) {
        setKey(directionReqDto);
        return sendRequest(googleDirectionsEndpoint, HttpMethods.GET, directionReqDto);
    }

    @Override
    protected void setKey(GenericRequestDTO source) {
        source.setApiKey(apiKey);
    }
}
