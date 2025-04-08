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
    private String travel_mode;
    private String maneuver;
    @Builder.Default
    private List<Steps> steps = new ArrayList<>();
}
