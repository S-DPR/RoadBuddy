package org.hansung.roadbuddy.dto.taxi.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AverageResDto {
    private String runDate;
    private int carCnt;
    private int callCnt;
    private int waitTime;
    private double pickupTime;
    private int rideDist;
    private int fare;
}
