package org.hansung.roadbuddy.dto.google.response.googleDirections;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class TransmitDetails {
    private Map arrival_stop;
    private Map arrival_time;
    private Map departure_stop;
    private Map departure_time;
    private String headsign;
    private Line line;
    private String num_stops;
}
