package org.hansung.roadbuddy.dto.google.response.googleDirections;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class GeocodedWaypoints {
    private String geocoder_status;
    private String place_id;
    private List<String> types;
}
