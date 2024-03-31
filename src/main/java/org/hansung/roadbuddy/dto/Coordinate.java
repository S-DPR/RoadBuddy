package org.hansung.roadbuddy.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Coordinate {
    private Double longitude;
    private Double latitude;

    public String toGoogleString() {
        return latitude + "," + longitude;
    }
}
