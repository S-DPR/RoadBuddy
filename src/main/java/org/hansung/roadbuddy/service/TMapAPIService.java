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
import org.hansung.roadbuddy.utilService.DtoParseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TMapAPIService extends GenericAPIService {
    private final String apiKey;
    private final String tMapDirectionEndpoint = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1&callback=function";
    private final String tMapDriveDirectionEndpoint = "https://apis.openapi.sk.com/tmap/routes?version=1";

    @Autowired
    TMapAPIService(ObjectMapper objectMapper, @Value("${api.key.tmap}") String apiKey) {
        super(objectMapper, null);
        this.apiKey = apiKey;
    }

    @Override
    protected HttpRequest.Builder createHttpRequestBuilder() {
        return HttpRequest.newBuilder()
                .header("appKey", apiKey);
    }

//    @Cacheable(value="TMapDirection")
//    public TMapDirectionsResDto getDirection(TMapDirectionReqDto tMapDirectionReqDto) throws JsonProcessingException {
//        System.out.println(tMapDirectionReqDto.hashCode());
//        System.out.println("tMapDirectionReqDto = " + tMapDirectionReqDto);
//        return getDirectionAsync(tMapDirectionReqDto).join();
//    }
//
//    @Async
//    public CompletableFuture<TMapDirectionsResDto> getDirectionAsync(TMapDirectionReqDto tMapDirectionReqDto) throws JsonProcessingException {
//        setKey(tMapDirectionReqDto);
//        String response = sendRequest(tMapDirectionEndpoint, HttpMethods.POST, createHttpRequestBuilder(), tMapDirectionReqDto);
//        System.out.println(response);
//        return CompletableFuture.completedFuture(objectMapper.readValue(response, TMapDirectionsResDto.class));
//    }

    public TMapDirectionsResDto getDirection(TMapDirectionReqDto tMapDirectionReqDto) throws JsonProcessingException {
        setKey(tMapDirectionReqDto);
        if (cache.containsKey(tMapDirectionReqDto)) return (TMapDirectionsResDto) cache.get(tMapDirectionReqDto);
        String response = sendRequest(tMapDirectionEndpoint, HttpMethods.POST, createHttpRequestBuilder(), tMapDirectionReqDto).replaceAll("\\p{Cntrl}", "");
        System.out.println("response = " + response);
        TMapDirectionsResDto ret = objectMapper.readValue(response, TMapDirectionsResDto.class);
        cache.putIfAbsent(tMapDirectionReqDto, ret);
        return ret;
    }

    public Map getDriveDirection(TMapDirectionReqDto tMapDirectionReqDto) throws JsonProcessingException {
        setKey(tMapDirectionReqDto);
        tMapDirectionReqDto.setSearchOption(0L);
        if (cache.containsKey(tMapDirectionReqDto)) return (Map) cache.get(tMapDirectionReqDto);
        String response = sendRequest(tMapDriveDirectionEndpoint, HttpMethods.POST, createHttpRequestBuilder(), tMapDirectionReqDto);
        System.out.println("response = " + response);
        Map ret = objectMapper.readValue(response, Map.class);
        cache.putIfAbsent(tMapDirectionReqDto, ret);
        return ret;
    }

    // GoogleDirectionResDto 받아 WALKING 부분 업데이트
    public GoogleDirectionResDto updateWalkingStepsInGoogleDirection(GoogleDirectionResDto googleDirectionResDto) {
        googleDirectionResDto.getRoutes().parallelStream().forEach(route -> {
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
        if (tMapDirectionsResDto.getFeatures() == null) throw new NullPointerException("NullptrError, 사용량 부족 의심");

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

}
