package org.hansung.roadbuddy.dto.google.response.googleDirections;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import okhttp3.Route;
import org.hansung.roadbuddy.dto.Coordinate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class RoutesCoordinate {
    private Double lat;
    private Double lng;

    public static RoutesCoordinate coordinateToRoutesCoordinate(Coordinate coordinate) {
        RoutesCoordinate routesCoordinate = new RoutesCoordinate();
        routesCoordinate.setLat(coordinate.getLatitude());
        routesCoordinate.setLng(coordinate.getLongitude());
        return routesCoordinate;
    }

    public static Coordinate routesCoordinateToCoordinate(RoutesCoordinate routesCoordinate) {
        Coordinate coordinate = new Coordinate();
        coordinate.setLatitude(routesCoordinate.getLat());
        coordinate.setLongitude(routesCoordinate.getLng());
        return coordinate;
    }
}
