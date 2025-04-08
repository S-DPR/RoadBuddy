package org.hansung.roadbuddy.dto.steepSlope;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode(of = {"line", "station"})
public class SubwaySleepSlopeDto {
    private String line;
    private String station;
    private List<SteepSlope> steepSlope;
}
