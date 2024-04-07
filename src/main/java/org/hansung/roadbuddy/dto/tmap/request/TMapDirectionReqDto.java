package org.hansung.roadbuddy.dto.tmap.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hansung.roadbuddy.dto.Coordinate;
import org.hansung.roadbuddy.generic.GenericPostRequestDTO;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@ToString
public class TMapDirectionReqDto extends GenericPostRequestDTO {
    private Coordinate start;
    private Coordinate end;
    private String reqCoordType = "WGS84GEO";
    private String startName = "출발지";
    private String endName = "도착지";
    private Long searchOption = 30L;

    public String getApiKeyDisplayName() {
        return "appKey";
    }

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
