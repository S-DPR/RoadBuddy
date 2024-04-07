package org.hansung.roadbuddy.dto.tmap.response.tmapDirections;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Properties {
    private Long totalDistance;
    private Long totalTime;
    private Long index;
    private Long pointIndex;
    private String name;
    private String description;
    private String direction;
    private String nearPoiName;
    private String nearPoiX;
    private String nearPoiY;
    private String intersectionName;
    private String facilityType;
    private String facilityName;
    private Long turnType;
    private String pointType;

    public String timeToString() {
        long min = (totalTime + 59) / 60;
        long hour = min / 60;
        min = min % 60;

        if (hour > 0) {
            return String.format("%d시간\n%d분", hour, min);
        } else {
            return String.format("%d분", min);
        }
    }
}
