package org.hansung.roadbuddy.dto.naver.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hansung.roadbuddy.dto.Coordinate;

import java.util.List;

@Getter
@Setter
@ToString
public class PlaceSearchResDto {
    private String lastBuildDate;
    private String total;
    private String start;
    private String display;
    private List<PlaceSearchItem> items;
    private Coordinate coordinate = null;
}
