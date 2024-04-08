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
//    private Map<String, Object> additionalProperties = new HashMap<>();
//
//    @JsonAnySetter
//    public void setAdditionalProperty(String name, Object value) {
//        this.additionalProperties.put(name, value);
//    }

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

    public static Steps featureToSteps(Features features) {
        Geometry geometry = features.getGeometry();
        Properties properties = features.getProperties();

        Distance distance = new Distance(properties.getTotalDistance());
        Duration duration = new Duration(properties.getTotalTime());
        Coordinate startLocation = geometry.getFirstCoordinate();
        Coordinate endLocation = geometry.getLastCoordinate();

        // TODO htmlinstructions, maneuver 표시해줘야함
        return Steps.builder()
                .distance(distance)
                .duration(duration)
                .end_location(RoutesCoordinate.coordinateToRoutesCoordinate(endLocation))
                .polyline(coordinationToPolyline(geometry))
                .start_location(RoutesCoordinate.coordinateToRoutesCoordinate(startLocation))
                .travel_mode("WALKING")
                .build();
    }

    public static List<LatLng> extractCoordinationInGeometry(Geometry geometry) {
        List<LatLng> coords = new ArrayList<>();
        if (geometry.getType().equals("Point")) return coords;
        geometry.getCoordinates().forEach(i -> {
            Double lng = i.get(0);
            Double lat = i.get(1);
            coords.add(new LatLng(lat, lng));
        });
        return coords;
    }

    public static Polyline coordinationToPolyline(Geometry geometry) {
        List<LatLng> coords = extractCoordinationInGeometry(geometry);
        String polylineEncode = PolylineEncoding.encode(coords);
        Polyline polyline = new Polyline();
        polyline.setPoints(polylineEncode);
        return polyline;
    }
}
