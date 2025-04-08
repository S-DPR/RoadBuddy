package org.hansung.roadbuddy.dto.tmap.response.tmapDirections;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TMapDirectionsResDto {
    private String type;
    private List<Features> features;
}
