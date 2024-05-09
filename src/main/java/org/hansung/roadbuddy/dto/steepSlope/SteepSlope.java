package org.hansung.roadbuddy.dto.steepSlope;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class SteepSlope {
    private String address;
    private String shortAddress;
    private Double latitude;
    private Double longitude;
    //private Double dist;
}

