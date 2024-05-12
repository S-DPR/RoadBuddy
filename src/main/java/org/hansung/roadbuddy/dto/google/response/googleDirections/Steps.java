package org.hansung.roadbuddy.dto.google.response.googleDirections;

import lombok.*;
import org.hansung.roadbuddy.dto.rail.response.RailTransferResDto;
import org.hansung.roadbuddy.dto.steepSlope.SteepSlope;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Steps {
    private Distance distance;
    private Duration duration;
    private RoutesCoordinate end_location;
    private String html_instructions;
    private Polyline polyline;
    private RoutesCoordinate start_location;
    private TransmitDetails transit_details;
    private List<SteepSlope> steep_slope = new ArrayList<>();
    private List<RailTransferResDto<List<String>>> transfer_path = new ArrayList<>();
    private String travel_mode;
    private String maneuver;
    @Builder.Default
    private List<Steps> steps = new ArrayList<>();

    public void updateDistance() {
        if (steps.isEmpty()) return;
        steps.forEach(Steps::updateDistance);
        distance = new Distance(steps.stream().mapToLong(i -> i.getDistance().getValue()).sum());
    }

    public void updateDuration() {
        if (steps.isEmpty()) return;
        steps.forEach(Steps::updateDuration);
        duration = new Duration(steps.stream().mapToLong(i -> i.getDuration().getValue()).sum());
    }
}
