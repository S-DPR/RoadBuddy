package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        if (containsTrainRoute(googleDirectionResDto)) {
            log.info("응답에 기차 포함됨");
        }

        if (containsNullNumStops(googleDirectionResDto)) {
            log.warn("NumStops가 null인 경우 존재");
        }
        return googleDirectionResDto;
    }

    // TRANSIT 중 '기차'를 포함하는지 여부 판단
    private boolean containsTrainRoute(GoogleDirectionResDto googleDirectionResDto) {
        return googleDirectionResDto.getRoutes().stream().anyMatch(route -> {
            return route.getLegs().stream().anyMatch(leg -> {
                return leg.getSteps().stream().anyMatch(step -> {
                    return step.getTravel_mode().equals("TRANSIT") &&
                            step.getTransit_details()
                                    .getLine()
                                    .getVehicle()
                                    .getName()
                                    .equals("기차");
                });
            });
        });
    }

    // NumStops가 null인 경우가 있어 실제 서비스 적용 시 주의 필요합니다.
    private boolean containsNullNumStops(GoogleDirectionResDto googleDirectionResDto) {
        return googleDirectionResDto.getRoutes().stream().anyMatch(route -> {
            return route.getLegs().stream().anyMatch(leg -> {
                return leg.getSteps().stream().anyMatch(step -> {
                    return isSubway(step) && step.getTransit_details().getNum_stops() == null;
                });
            });
        });
    }
}
