package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import org.hansung.roadbuddy.dto.Coordinate;
import org.hansung.roadbuddy.dto.google.response.googleDirections.GoogleDirectionResDto;
import org.hansung.roadbuddy.dto.google.response.googleDirections.Polyline;
import org.hansung.roadbuddy.dto.google.response.googleDirections.Steps;
import org.hansung.roadbuddy.dto.tmap.response.tmapDirections.Properties;
import org.hansung.roadbuddy.dto.tmap.response.tmapDirections.TMapDirectionsResDto;
import org.hansung.roadbuddy.dto.tmap.request.TMapDirectionReqDto;
import org.hansung.roadbuddy.enums.HttpMethods;
import org.hansung.roadbuddy.generic.GenericAPIService;
import org.hansung.roadbuddy.generic.GenericRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public GoogleDirectionResDto convertGoogleWalkToTMapWalkDirection(GoogleDirectionResDto googleDirectionResDto) {
        GoogleDirectionResDto ret = new GoogleDirectionResDto();
        ret.setGeocoded_waypoints(googleDirectionResDto.getGeocoded_waypoints());
        ret.setRoutes(googleDirectionResDto.getRoutes());

        ret.getRoutes().forEach(route -> {
            List<Steps> newSteps = new ArrayList<>();
            route.getLegs().forEach(leg -> {
                leg.getSteps().forEach(step -> {
                    try {
                        step = walkingStepFinder(step);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    if (step != null) {
                        newSteps.add(step);
                    }
                });
                leg.setSteps(newSteps);
                leg.setTotalDistance();
                leg.setTotalTime();
            });
//            route.updateOverviewPolyline();
        });
        return ret;
    }

    private Steps walkingStepFinder(Steps steps) throws JsonProcessingException {
        if (steps.getTravel_mode().equals("WALKING")) {
            steps = walkingStepChanger(steps);
        }
        if (steps == null) return null;

        List<Steps> newSteps = new ArrayList<>();
        if (steps.getSteps() != null) {
            steps.getSteps().forEach( step -> {
                if (step.getTravel_mode().equals("WALKING")) {
                    try {
                        Steps change = walkingStepChanger(step);
                        if (change != null) {
                            step = change;
                        }
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }
                newSteps.add(step);
            });
        }
        steps.setSteps(newSteps);
        return steps;
    }

    private Steps walkingStepChanger(Steps step) throws JsonProcessingException {
        if (step.getStart_location().equals(step.getEnd_location())) return null;
        TMapDirectionReqDto tMapDirectionReqDto = new TMapDirectionReqDto();
        tMapDirectionReqDto.setStart(Coordinate.routesCoordinateToCoordinate(step.getStart_location()));
        tMapDirectionReqDto.setEnd(Coordinate.routesCoordinateToCoordinate(step.getEnd_location()));

        TMapDirectionsResDto tMapDirectionsResDto = getDirection(tMapDirectionReqDto);

        List<LatLng> routes = new ArrayList<>();
        Steps res = Steps.featureToSteps(tMapDirectionsResDto.getFeatures().get(0)); // feature의 0번째는 항상 전체를 아우르는 overview
        tMapDirectionsResDto.getFeatures().forEach(i -> {
            routes.addAll(Steps.extractCoordinationInGeometry(i.getGeometry()));
        });
        res.setPolyline(new Polyline(PolylineEncoding.encode(routes)));

        Properties properties = tMapDirectionsResDto.getFeatures().get(0).getProperties();
        res.getDistance().setValueAndText(properties.getTotalDistance());
        res.getDuration().setValueAndText(properties.getTotalTime());
        return res;
    }

    @Override
    protected void setKey(GenericRequestDTO dto) {
        dto.setApiKey(apiKey);
    }
}
