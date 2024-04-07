package org.hansung.roadbuddy.dto.google.response.googleDirections;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class Routes {
    private Map bounds;
    private String copyrights;
    private List<Legs> legs;
//    private Polyline overview_polyline;
//    private String summary;
//    private List<String> warnings;
//    private List waypoint_order;

//    public void updateOverviewPolyline() {
//        List<LatLng> coords = new ArrayList<>();
//        legs.forEach(leg -> {
//            leg.getSteps().forEach(step -> {
//                coords.addAll(PolylineEncoding.decode(step.getPolyline().getPoints()));
//            });
//        });
//        coords.forEach(System.out::println);
//        overview_polyline = new Polyline(PolylineEncoding.encode(coords));
//    }
}
