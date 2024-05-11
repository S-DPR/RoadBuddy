package org.hansung.roadbuddy.service;

import org.hansung.roadbuddy.dto.google.response.googleDirections.Legs;
import org.hansung.roadbuddy.dto.google.response.googleDirections.Steps;
import org.hansung.roadbuddy.dto.steepSlope.SteepSlope;
import org.hansung.roadbuddy.dto.steepSlope.SteepSlopePool;
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
            if (isSubway(step)) {
                step.setSteep_slope(new ArrayList<>());
                step.getSteep_slope().addAll(getDepartureSteepSlopeBySubway(step));
                step.getSteep_slope().addAll(getArrivalSteepSlopeBySubway(step));
            }
        });
        return legs;
    }

    // line과 station에 해당하는 가파른 경사 데이터를 반환하는 메서드
    public List<SteepSlope> getDepartureSteepSlopeBySubway(Steps steps) {
        String line = steps.getTransit_details().getLine().getShort_name();
        String station = (String) steps.getTransit_details().getDeparture_stop().get("name");
        System.out.println("line station = " + line + " " + station);
        return steepSlopePool.get(line, station);
    }

    public List<SteepSlope> getArrivalSteepSlopeBySubway(Steps steps) {
        String line = steps.getTransit_details().getLine().getShort_name();
        String station = (String) steps.getTransit_details().getArrival_stop().get("name");
        System.out.println("line station = " + line + " " + station);
        return steepSlopePool.get(line, station);
    }

    private boolean isSubway(Steps step) {
        if (!step.getTravel_mode().equals("TRANSIT")) return false;
        return step.getTransit_details().getLine().getVehicle().getType().equals("SUBWAY");
    }
}
