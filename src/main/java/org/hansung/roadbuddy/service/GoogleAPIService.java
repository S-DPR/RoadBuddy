package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hansung.roadbuddy.dto.google.request.AddressSearchReqDto;
import org.hansung.roadbuddy.dto.google.request.GoogleDirectionReqDto;
import org.hansung.roadbuddy.dto.google.request.GeocodingReqDto;
import org.hansung.roadbuddy.dto.google.request.TextSearchReqDto;
import org.hansung.roadbuddy.dto.google.response.googleDirections.GoogleDirectionResDto;
import org.hansung.roadbuddy.dto.google.response.googleDirections.Routes;
import org.hansung.roadbuddy.dto.utils.SubwayInfo;
import org.hansung.roadbuddy.dto.utils.SubwayInfoPool;
import org.hansung.roadbuddy.generic.GenericAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.hansung.roadbuddy.utilService.DtoGetUtils.isSubway;

@Service
public class GoogleAPIService extends GenericAPIService {
    private final String geoCodingEndpoint = "https://maps.googleapis.com/maps/api/geocode/json";
    private final String googlePlaceEndpoint = "https://maps.googleapis.com/maps/api/place/autocomplete/json";
    private final String googleDirectionsEndpoint = "https://maps.googleapis.com/maps/api/directions/json";
    private final String googleTextSearch = "https://maps.googleapis.com/maps/api/place/textsearch/json";
    private final SubwayInfoPool subwayInfoPool;

    @Autowired
    GoogleAPIService(ObjectMapper objectMapper, @Value("${api.key.google}") String apiKey, SubwayInfoPool subwayInfoPool) {
        super(objectMapper, apiKey);
        this.subwayInfoPool = subwayInfoPool;
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
        String response = sendRequest(googleDirectionsEndpoint, directionReqDto);
        GoogleDirectionResDto googleDirectionResDto = objectMapper.readValue(response, GoogleDirectionResDto.class);
        delTrain(googleDirectionResDto);
        updateNullNumStops(googleDirectionResDto);
        return googleDirectionResDto;
    }

    private void delTrain(GoogleDirectionResDto googleDirectionResDto) {
        List<Routes> routes = new ArrayList<>();
        googleDirectionResDto.getRoutes().forEach(route -> {
            boolean hasTrain = route.getLegs().stream().anyMatch(leg -> {
                return leg.getSteps().stream().anyMatch(step -> {
                    return step.getTravel_mode().equals("TRANSIT") &&
                            step.getTransit_details()
                                    .getLine()
                                    .getVehicle()
                                    .getName()
                                    .equals("기차");
                });
            });
            if (!hasTrain) routes.add(route);
        });
        googleDirectionResDto.setRoutes(routes);
    }

    private void updateNullNumStops(GoogleDirectionResDto googleDirectionResDto) {
        googleDirectionResDto.getRoutes().forEach(route -> {
            route.getLegs().forEach(leg -> {
                leg.getSteps().forEach(step -> {
                    if (isSubway(step) && step.getTransit_details().getNum_stops() == null) {
                        SubwayInfo start = subwayInfoPool.getByStepWithDeparture(step);
                        SubwayInfo end = subwayInfoPool.getByStepWithArrival(step);
                        step.getTransit_details().setNum_stops(start.findStationShortestDistance(end)+"");
                        System.out.println("step.getTransit_details().getNum_stops() = " + step.getTransit_details().getNum_stops());
                    }
                });
            });
        });
    }
}
