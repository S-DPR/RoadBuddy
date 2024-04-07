package org.hansung.roadbuddy.dto.tmap.response.tmapDirections;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Features {
    private String type;
    private Geometry geometry;
    private Properties properties;
}
