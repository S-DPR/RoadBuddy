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

        return Steps.builder()
                .distance(distance)
                .duration(duration)
                .end_location(RoutesCoordinate.coordinateToRoutesCoordinate(endLocation))
                .polyline(coordinationToPolyline(geometry))
                .start_location(RoutesCoordinate.coordinateToRoutesCoordinate(startLocation))
                .travel_mode("WALKING")
                .build();
    }

    public static List<Steps> featuresToSteps(List<Features> features) {
        List<Steps> res = new ArrayList<>();
        // point 뒤에 linestring이 있으면 point는 linestring을 수식하지만,
        // linestring 단독으로 나오면 turnType이 없어서 공백으로 채움
        // 그런데 마지막은 도착 Point 하나. 마지막으로 처리하기 좋게 되어있음.
        Features point = null;
        for (int idx = 0; idx < features.size(); idx++) {
            Features cur = features.get(idx);
            if (cur.getGeometry().getType().equals("Point")) {
                point = cur;
                continue;
            }
            Features lineString = features.get(idx);
            res.add(pointAndLineStringToSteps(point, lineString));
            point = null;
        }
        return res;
    }

    public static Steps pointAndLineStringToSteps(Features point, Features lineString) {
        Properties lineStringProperties = lineString.getProperties();
        String turnType, description;
        if (point == null) { // linestring에 대한 설명이 없음
            turnType = "";
            description = lineStringProperties.getDescription();
        } else {
            turnType = point.getProperties().getTurnType().toString();
            description = point.getProperties().getDescription();
        }
        List<LatLng> latlngs = DtoParseUtils.extractLatLngInGeometry(lineString.getGeometry());

        Distance distance = new Distance(lineStringProperties.getDistance());
        Duration duration = new Duration(lineStringProperties.getTime());

        return Steps.builder()
                .distance(distance)
                .duration(duration)
                .travel_mode("WALKING")
                .maneuver(turnType)
                .html_instructions(description)
                .polyline(new Polyline(PolylineEncoding.encode(latlngs)))
                .end_location(RoutesCoordinate.coordinateToRoutesCoordinate(lineString.getGeometry().getLastCoordinate()))
                .start_location(RoutesCoordinate.coordinateToRoutesCoordinate(lineString.getGeometry().getFirstCoordinate()))
                .build();
    }
}
