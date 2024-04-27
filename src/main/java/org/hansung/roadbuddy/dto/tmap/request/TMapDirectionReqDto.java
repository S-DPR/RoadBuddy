package org.hansung.roadbuddy.dto.tmap.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hansung.roadbuddy.dto.Coordinate;
import org.hansung.roadbuddy.generic.GenericRequestDTO;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class TMapDirectionReqDto extends GenericRequestDTO {
    private Coordinate start;
    private Coordinate end;
    private String reqCoordType = "WGS84GEO";
    private String startName = "출발지";
    private String endName = "도착지";
    private Long searchOption = 30L;

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("startX", start.getLongitude().toString());
        map.put("startY", start.getLatitude().toString());
        map.put("endX", end.getLongitude().toString());
        map.put("endY", end.getLatitude().toString());
        map.put("reqCoordType", reqCoordType);
        map.put("startName", startName);
        map.put("endName", endName);
        map.put("searchOption", searchOption.toString());
        return map;
    }
}
