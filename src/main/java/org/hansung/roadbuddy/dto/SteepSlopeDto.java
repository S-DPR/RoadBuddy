package org.hansung.roadbuddy.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SteepSlopeDto {
    private String address;
    private String shortAddress;
    private Double latitude;
    private Double longitude;
    //private Double dist;

    public SteepSlopeDto(String address, String shortAddress, Double latitude, Double longitude) {
        this.address = address;
        this.shortAddress = shortAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}

