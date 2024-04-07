package org.hansung.roadbuddy.dto.tmap.response.tmapDirections;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hansung.roadbuddy.deserialization.TMapCoordinatesDeserializer;
import org.hansung.roadbuddy.dto.Coordinate;

import java.util.List;

@Setter
@Getter
@ToString
public class Geometry {
    private String type;
    @JsonDeserialize(using = TMapCoordinatesDeserializer.class)
    List<List<Double>> coordinates;

    public Coordinate getFirstCoordinate() {
        List<Double> coord = coordinates.get(0);
        return listToCoordinate(coord);
    }

    public Coordinate getLastCoordinate() {
        List<Double> coord = coordinates.get(coordinates.size()-1);
        return listToCoordinate(coord);
    }

    private Coordinate listToCoordinate(List<Double> coord) {
        Coordinate coordinate = new Coordinate();
        coordinate.setLongitude(coord.get(0));
        coordinate.setLatitude(coord.get(1));
        return coordinate;
    }
}
