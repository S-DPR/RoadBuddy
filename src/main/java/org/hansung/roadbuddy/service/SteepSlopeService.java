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

    public List<List<SteepSlope>> updateSteepSlopes(Legs legs) {
        List<List<SteepSlope>> steepSlopes = new ArrayList<>();
        for (int i = 0; i < legs.getSteps().size(); i++) {
            steepSlopes.add(new ArrayList<>());
            Steps step = legs.getSteps().get(i);
            if (DtoGetUtils.isSubway(step)) {
                List<SteepSlope> data = new ArrayList<>();
                data.addAll(getDepartureSteepSlopeBySubway(step));
                data.addAll(getArrivalSteepSlopeBySubway(step));
                steepSlopes.set(i, data.stream().distinct().toList());
            }
        };
        return steepSlopes;
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
