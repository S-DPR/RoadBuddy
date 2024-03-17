package org.hansung.roadbuddy.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Coordinate {
    double longitude;
    double latitude;
    public String toString() {
        return latitude+","+longitude;
    }
}
