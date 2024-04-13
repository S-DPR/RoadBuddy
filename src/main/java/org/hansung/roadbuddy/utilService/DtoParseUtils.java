package org.hansung.roadbuddy.utilService;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import org.hansung.roadbuddy.dto.Coordinate;
import org.hansung.roadbuddy.dto.google.response.googleDirections.*;
import org.hansung.roadbuddy.dto.tmap.response.tmapDirections.Features;
import org.hansung.roadbuddy.dto.tmap.response.tmapDirections.Geometry;
import org.hansung.roadbuddy.dto.tmap.response.tmapDirections.Properties;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DtoParseUtils {
    public static List<LatLng> extractLatLngInGeometry(Geometry geometry) {
        List<LatLng> coords = new ArrayList<>();
        geometry.getCoordinates().forEach(i -> {
            Double lng = i.get(0);
            Double lat = i.get(1);
            coords.add(new LatLng(lat, lng));
        });
        return coords;
    }

    public static Polyline coordinationToPolyline(Geometry geometry) {
        List<LatLng> coords = extractLatLngInGeometry(geometry);
        String polylineEncode = PolylineEncoding.encode(coords);
        Polyline polyline = new Polyline();
        polyline.setPoints(polylineEncode);
        return polyline;
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
}
