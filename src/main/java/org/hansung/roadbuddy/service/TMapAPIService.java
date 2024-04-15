package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import org.hansung.roadbuddy.dto.Coordinate;
import org.hansung.roadbuddy.dto.google.response.googleDirections.GoogleDirectionResDto;
import org.hansung.roadbuddy.dto.google.response.googleDirections.Polyline;
import org.hansung.roadbuddy.dto.google.response.googleDirections.Steps;
import org.hansung.roadbuddy.dto.tmap.response.tmapDirections.TMapDirectionsResDto;
import org.hansung.roadbuddy.dto.tmap.request.TMapDirectionReqDto;
import org.hansung.roadbuddy.enums.HttpMethods;
import org.hansung.roadbuddy.generic.GenericAPIService;
import org.hansung.roadbuddy.generic.GenericRequestDTO;
import org.hansung.roadbuddy.utilService.DtoParseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TMapAPIService extends GenericAPIService {
    private final String apiKey;
    private final String tMapDirectionEndpoint = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1&callback=function";

    @Autowired
    TMapAPIService(ObjectMapper objectMapper, @Value("${api.key.tmap}") String apiKey) {
        super(objectMapper);
        this.apiKey = apiKey;
    }

    public TMapDirectionsResDto getDirection(TMapDirectionReqDto tMapCoordinate) throws JsonProcessingException {
        setKey(tMapCoordinate);
        String response = sendRequest(tMapDirectionEndpoint, HttpMethods.POST, tMapCoordinate);
        return objectMapper.readValue(response, TMapDirectionsResDto.class);
    }

    // GoogleDirectionResDto 받아 WALKING 부분 업데이트
    public GoogleDirectionResDto updateWalkingStepsInGoogleDirection(GoogleDirectionResDto googleDirectionResDto) {
        googleDirectionResDto.getRoutes().forEach(route -> {
            route.getLegs().forEach(leg -> {
                List<Steps> updatedSteps = leg.getSteps().stream().map(step -> {
                    try {
                        return "WALKING".equals(step.getTravel_mode()) ? updateWalkingSteps(step) : step;
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
                leg.setSteps(updatedSteps);
                leg.updateTotalDistance();
                leg.updateTotalTime();
            });
        });
        return googleDirectionResDto;
    }

    // steps를 중 WALKING인 부분 업데이트 함수 호출. 내부 Steps도 재귀적으로 탐색
    private Steps updateWalkingSteps(Steps steps) throws JsonProcessingException {
        List<Steps> newSteps = steps.getSteps().stream().map(step -> {
            try {
                return step.getTravel_mode().equals("WALKING") ? updateWalkingSteps(step) : step;
            } catch (JsonProcessingException e) {
                throw new RuntimeException("JSON 관련 에러 발생", e); // TODO RTE던지긴 좀 그렇지 ㅋㅋ
            }
        }).toList();
        steps = walkingStepChanger(steps);
        steps.setSteps(newSteps);
        return steps;
    }

    // 실제 steps 변환 함수
    private Steps walkingStepChanger(Steps step) throws JsonProcessingException {
        if (step.getStart_location().equals(step.getEnd_location())) return step;
        TMapDirectionsResDto tMapDirectionsResDto = stepToTMapDirection(step);

        // feature의 0번째는 항상 전체를 아우르는 overview
        Steps res = DtoParseUtils.featureToSteps(tMapDirectionsResDto.getFeatures().get(0));
        res.setPolyline(getPolylineUsingFeatures(tMapDirectionsResDto));

        // 세부 steps 설정
        res.setSteps(DtoParseUtils.featuresToSteps(tMapDirectionsResDto.getFeatures()));
        return res;
    }

    // location 양 끝점 사용해 TMapDirections 호출, 결과 반환
    private TMapDirectionsResDto stepToTMapDirection(Steps step) throws JsonProcessingException {
        TMapDirectionReqDto tMapDirectionReqDto = new TMapDirectionReqDto();
        tMapDirectionReqDto.setStart(Coordinate.routesCoordinateToCoordinate(step.getStart_location()));
        tMapDirectionReqDto.setEnd(Coordinate.routesCoordinateToCoordinate(step.getEnd_location()));
        return getDirection(tMapDirectionReqDto);
    }

    // Walking Polyline 생성
    private Polyline getPolylineUsingFeatures(TMapDirectionsResDto tMapDirectionsResDto) {
        List<LatLng> routes = new ArrayList<>();
        tMapDirectionsResDto.getFeatures().forEach(i -> {
            routes.addAll(DtoParseUtils.extractLatLngInGeometry(i.getGeometry()));
        });
        return new Polyline(PolylineEncoding.encode(routes));
    }

    @Override
    protected void setKey(GenericRequestDTO dto) {
        dto.setApiKey(apiKey);
    }
}
