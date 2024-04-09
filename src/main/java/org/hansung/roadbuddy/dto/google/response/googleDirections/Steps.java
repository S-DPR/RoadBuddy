package org.hansung.roadbuddy.dto.google.response.googleDirections;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import lombok.*;
import org.hansung.roadbuddy.dto.Coordinate;
import org.hansung.roadbuddy.dto.tmap.response.tmapDirections.Features;
import org.hansung.roadbuddy.dto.tmap.response.tmapDirections.Geometry;
import org.hansung.roadbuddy.dto.tmap.response.tmapDirections.Properties;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private Map<String, Object> transit_details;
    private String travel_mode;
    private String maneuver;
    private List<Steps> steps;

    public void updateDistance() {
        if (steps == null || steps.isEmpty()) return;
        steps.forEach(Steps::updateDistance);
        distance = new Distance(steps.stream().mapToLong(i -> i.getDistance().getValue()).sum());
    }

    public void updateDuration() {
        if (steps == null || steps.isEmpty()) return;
        steps.forEach(Steps::updateDuration);
        duration = new Duration(steps.stream().mapToLong(i -> i.getDuration().getValue()).sum());
    }
}
