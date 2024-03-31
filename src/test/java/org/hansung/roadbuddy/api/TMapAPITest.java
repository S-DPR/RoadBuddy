package org.hansung.roadbuddy.api;

import org.hansung.roadbuddy.dto.Coordinate;
import org.hansung.roadbuddy.dto.tmap.TMapDirectionReqDto;
import org.hansung.roadbuddy.service.TMapAPIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class TMapAPITest {
    @Autowired
    private TMapAPIService tMapAPIService;

    @Test
    public void 도보경로검색() {
        TMapDirectionReqDto tMapDirectionReqDto = new TMapDirectionReqDto();
        Coordinate origin = new Coordinate();
        Coordinate destination = new Coordinate();
        origin.setLatitude(37.6150815);
        origin.setLongitude(127.0657675); // 석계역 위경도
        destination.setLatitude(37.58284829999999);
        destination.setLongitude(127.0105811); // 한성대학교 위경도
        tMapDirectionReqDto.setStart(origin);
        tMapDirectionReqDto.setEnd(destination);
        Map ret = tMapAPIService.getDirection(tMapDirectionReqDto);
        System.out.println(ret.size());
        ret.keySet().forEach(i -> {
            System.out.println(i + " " + ret.get(i));
        });
    }
}
