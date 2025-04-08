package org.hansung.roadbuddy.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hansung.roadbuddy.dto.google.response.googleDirections.RoutesCoordinate;

import java.util.List;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class Coordinate {
    private Double longitude;
    private Double latitude;

    public String toGoogleString() {
        return latitude + "," + longitude;
    }

    public static Coordinate routesCoordinateToCoordinate(RoutesCoordinate routesCoordinate) {
        Coordinate coordinate = new Coordinate();
        coordinate.setLatitude(routesCoordinate.getLat());
        coordinate.setLongitude(routesCoordinate.getLng());
        return coordinate;
    }

    public static Coordinate listToCoordinate(List<Double> coord) {
        Coordinate coordinate = new Coordinate();
        coordinate.setLongitude(coord.get(0));
        coordinate.setLatitude(coord.get(1));
        return coordinate;
    }
}
