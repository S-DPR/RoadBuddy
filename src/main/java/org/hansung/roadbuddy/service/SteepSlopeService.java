package org.hansung.roadbuddy.service;

import org.hansung.roadbuddy.dto.google.response.googleDirections.Legs;
import org.hansung.roadbuddy.dto.google.response.googleDirections.Steps;
import org.hansung.roadbuddy.dto.steepSlope.SteepSlope;
import org.hansung.roadbuddy.dto.steepSlope.SteepSlopePool;
import org.hansung.roadbuddy.utilService.DtoGetUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SteepSlopeService {
    private final SteepSlopePool steepSlopePool;

    public SteepSlopeService(SteepSlopePool steepSlopePool) {
        this.steepSlopePool = steepSlopePool;
    }

    public Legs updateSteepSlopes(Legs legs) {
        legs.getSteps().forEach(step -> {
            if (DtoGetUtils.isSubway(step)) {
                step.setSteep_slope(new ArrayList<>());
                step.getSteep_slope().addAll(getDepartureSteepSlopeBySubway(step));
                step.getSteep_slope().addAll(getArrivalSteepSlopeBySubway(step));
                step.setSteep_slope(step.getSteep_slope().stream().distinct().toList());
            }
        });
        return legs;
    }

    // line과 station에 해당하는 가파른 경사 데이터를 반환하는 메서드
    public List<SteepSlope> getDepartureSteepSlopeBySubway(Steps steps) {
        String line = DtoGetUtils.getLineShortName(steps);
        String station = DtoGetUtils.getDepartureStop(steps);
        return steepSlopePool.get(line, station);
    }

    public List<SteepSlope> getArrivalSteepSlopeBySubway(Steps steps) {
        String line = DtoGetUtils.getLineShortName(steps);
        String station = DtoGetUtils.getArrivalStop(steps);
        return steepSlopePool.get(line, station);
    }
}
