package org.hansung.roadbuddy.dto.rail.utils;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class SubwayInfo {
    private String line;
    private String station;

}
