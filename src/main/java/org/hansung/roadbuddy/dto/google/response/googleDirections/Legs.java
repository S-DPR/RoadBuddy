package org.hansung.roadbuddy.dto.google.response.googleDirections;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Legs {
    private LegsInfo arrival_time;
    private LegsInfo departure_time;
    private Distance distance;
    private Duration duration;
    private String end_address;
    private RoutesCoordinate end_location;
    private String start_address;
    private RoutesCoordinate start_location;
    private List<Steps> steps;
}
