package org.hansung.roadbuddy.api;

import org.hansung.roadbuddy.dto.Coordinate;
import org.hansung.roadbuddy.dto.google.AddressSearchReqDto;
import org.hansung.roadbuddy.dto.google.GoogleDirectionReqDto;
import org.hansung.roadbuddy.dto.google.GeocodingReqDto;
import org.hansung.roadbuddy.service.GoogleAPIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class GoogleAPITest {
    @Autowired GoogleAPIService googleAPIService;

    @Test
    public void 주소검색() {
        AddressSearchReqDto addressSearchReqDto = new AddressSearchReqDto();
        addressSearchReqDto.setInput("서울");
        Map ret = googleAPIService.getSimilarAddressList(addressSearchReqDto);
        ret.keySet().forEach(i -> {
            System.out.println(i + " " + ret.get(i));
        });
    }

    @Test
    public void 지오코딩() {
        GeocodingReqDto geocodingReqDto = new GeocodingReqDto();
        geocodingReqDto.setAddress("서울역앞");
        Map ret = googleAPIService.getAddressCoordinates(geocodingReqDto);
        ret.keySet().forEach(i -> {
            System.out.println(i + " " + ret.get(i));
        });
    }

    @Test
    public void 대중교통검색() {
        GoogleDirectionReqDto directionReqDto = new GoogleDirectionReqDto();
        Coordinate origin = new Coordinate();
        Coordinate destination = new Coordinate();
        origin.setLatitude(37.6150815);
        origin.setLongitude(127.0657675); // 석계역 위경도
        destination.setLatitude(37.58284829999999);
        destination.setLongitude(127.0105811); // 한성대학교 위경도
        directionReqDto.setOrigin(origin);
        directionReqDto.setDestination(destination);
        Map ret = googleAPIService.getDirection(directionReqDto);
        ret.keySet().forEach(i -> {
            System.out.println(i + " " + ret.get(i));
        });
    }
}
