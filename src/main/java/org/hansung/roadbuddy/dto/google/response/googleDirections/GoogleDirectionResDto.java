package org.hansung.roadbuddy.dto.google.response.googleDirections;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GoogleDirectionResDto {
    List<GeocodedWaypoints> geocoded_waypoints;
    List<Routes> routes;
}
