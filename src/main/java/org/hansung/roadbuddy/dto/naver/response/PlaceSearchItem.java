package org.hansung.roadbuddy.dto.naver.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class PlaceSearchItem {
    private String title;
    private String link;
    private String category;
    private String description;
    private String telephone;
    private String address;
    private String roadAddress;
    private String mapx;
    private String mapy;
    private Map<String, Object> geocoding = null;
}
