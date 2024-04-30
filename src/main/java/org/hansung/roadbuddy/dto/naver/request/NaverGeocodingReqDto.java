package org.hansung.roadbuddy.dto.naver.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hansung.roadbuddy.dto.Coordinate;
import org.hansung.roadbuddy.generic.GenericRequestDTO;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class NaverGeocodingReqDto extends GenericRequestDTO {
    private String query;
    private Coordinate coordinate = null;

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("query", query);
        if (coordinate != null) {
            map.put("coordinate", coordinate.getLongitude() + "," + coordinate.getLatitude());
        }
        return map;
    }
}
