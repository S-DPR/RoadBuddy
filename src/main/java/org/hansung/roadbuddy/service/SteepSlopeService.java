package org.hansung.roadbuddy.service;

import org.hansung.roadbuddy.dto.Coordinate;

import java.util.HashMap;
import java.util.List;

public class SteepSlopeService {
    class SteepSlopeDto {
        String address;
        String shortAddress;
        Coordinate coordinate;
        Double dist;
    }
    private final HashMap<String, List<SteepSlopeDto>> map = new HashMap<>();

    SteepSlopeService() {
        // 파일 입출력으로 map 초기화. 키도 dto면 좋기는한데 어떻게하든 작동만 되게 하면 됨
    }

    public List<SteepSlopeDto> getSteepSlopeBySubway(int line, String station) {
        // line, station 키로 잘 집어넣어서 결과 반환.
    }
}
