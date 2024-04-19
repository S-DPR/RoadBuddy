package org.hansung.roadbuddy.dto.naver.request;

import lombok.Getter;
import lombok.Setter;
import org.hansung.roadbuddy.dto.Coordinate;
import org.hansung.roadbuddy.generic.GenericRequestDTO;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class PlaceSearchReqDto extends GenericRequestDTO {
    private String query;
    private Integer display = 10;
    private Integer start = 1;
    private Coordinate coordinate = null;

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("query", query);
        map.put("display", display.toString());
        map.put("start", start.toString());
        return map;
    }
}
