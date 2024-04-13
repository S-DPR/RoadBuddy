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
        if (coordinates.isEmpty()) return null;
        List<Double> coord = coordinates.get(0);
        return Coordinate.listToCoordinate(coord);
    }

    public Coordinate getLastCoordinate() {
        if (coordinates.isEmpty()) return null;
        List<Double> coord = coordinates.get(coordinates.size()-1);
        return Coordinate.listToCoordinate(coord);
    }

}
