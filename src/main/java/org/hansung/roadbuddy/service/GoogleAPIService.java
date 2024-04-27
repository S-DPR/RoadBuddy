package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hansung.roadbuddy.dto.google.request.AddressSearchReqDto;
import org.hansung.roadbuddy.dto.google.request.GoogleDirectionReqDto;
import org.hansung.roadbuddy.dto.google.request.GeocodingReqDto;
import org.hansung.roadbuddy.dto.google.request.TextSearchReqDto;
import org.hansung.roadbuddy.dto.google.response.googleDirections.GoogleDirectionResDto;
import org.hansung.roadbuddy.generic.GenericAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GoogleAPIService extends GenericAPIService {
    private final String geoCodingEndpoint = "https://maps.googleapis.com/maps/api/geocode/json";
    private final String googlePlaceEndpoint = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
    private final String googleDirectionsEndpoint = "https://maps.googleapis.com/maps/api/directions/json";
    private final String googleTextSearch = "https://maps.googleapis.com/maps/api/place/textsearch/json";

    @Autowired
    GoogleAPIService(ObjectMapper objectMapper, @Value("${api.key.google}") String apiKey) {
        super(objectMapper, apiKey);
    }

    public Map getAddressCoordinates(GeocodingReqDto geocodingReqDto) throws JsonProcessingException {
        setKey(geocodingReqDto);
        String response = sendRequest(geoCodingEndpoint, geocodingReqDto);
        return objectMapper.readValue(response, Map.class);
    }

    public Map getSimilarAddressList(AddressSearchReqDto addressSearchReqDto) throws JsonProcessingException {
        setKey(addressSearchReqDto);
        String response = sendRequest(googlePlaceEndpoint, addressSearchReqDto);
        return objectMapper.readValue(response, Map.class);
    }

    public Map getTextSearch(TextSearchReqDto textSearchReqDto) throws JsonProcessingException {
        setKey(textSearchReqDto);
        String response = sendRequest(googleTextSearch, textSearchReqDto);
        return objectMapper.readValue(response, Map.class);
    }

    public GoogleDirectionResDto getDirection(GoogleDirectionReqDto directionReqDto) throws JsonProcessingException {
        setKey(directionReqDto);
        if (cache.containsKey(directionReqDto)) return (GoogleDirectionResDto) cache.get(directionReqDto);
        String response = sendRequest(googleDirectionsEndpoint, directionReqDto);
        GoogleDirectionResDto ret = objectMapper.readValue(response, GoogleDirectionResDto.class);
        cache.putIfAbsent(directionReqDto, ret);
        return ret;
    }
}
