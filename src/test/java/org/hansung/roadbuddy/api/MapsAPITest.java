package org.hansung.roadbuddy.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.LatLng;
import org.hansung.roadbuddy.dto.Coordinate;
import org.hansung.roadbuddy.dto.google.request.AddressSearchReqDto;
import org.hansung.roadbuddy.dto.google.request.GoogleDirectionReqDto;
import org.hansung.roadbuddy.dto.google.request.GeocodingReqDto;
import org.hansung.roadbuddy.dto.google.response.googleDirections.GoogleDirectionResDto;
import org.hansung.roadbuddy.dto.google.response.googleDirections.Polyline;
import org.hansung.roadbuddy.dto.google.response.googleDirections.Steps;
import org.hansung.roadbuddy.dto.tmap.request.TMapDirectionReqDto;
import org.hansung.roadbuddy.dto.tmap.response.tmapDirections.TMapDirectionsResDto;
import org.hansung.roadbuddy.service.GoogleAPIService;
import org.hansung.roadbuddy.service.TMapAPIService;
import org.hansung.roadbuddy.utilService.DtoParseUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
class MapsAPITest {
    enum 좌표저장소 {
        한성대학교(37.58284829999999, 127.0105811),
        석계역(37.6150815, 127.0657675),
        마곡나루(37.5669356, 126.8265611);
        private final Coordinate coordinate;
        좌표저장소(Double lat, Double lon) {
            this.coordinate = new Coordinate();
            this.coordinate.setLatitude(lat);
            this.coordinate.setLongitude(lon);
        }

        public Coordinate getCoordinate() {
            return this.coordinate;
        }
    }

    @Autowired GoogleAPIService googleAPIService;
    @Autowired TMapAPIService tMapAPIService;

    @Test
    public void 주소검색() throws JsonProcessingException {
        AddressSearchReqDto addressSearchReqDto = new AddressSearchReqDto();
        addressSearchReqDto.setInput("서울");
        Map ret = googleAPIService.getSimilarAddressList(addressSearchReqDto);
        ret.keySet().forEach(i -> {
            System.out.println(i + " " + ret.get(i));
        });
    }

    @Test
    public void 지오코딩() throws JsonProcessingException {
        GeocodingReqDto geocodingReqDto = new GeocodingReqDto();
        geocodingReqDto.setAddress("서울역앞");
        Map ret = googleAPIService.getAddressCoordinates(geocodingReqDto);
        ret.keySet().forEach(i -> {
            System.out.println(i + " " + ret.get(i));
        });
    }

    @Test
    public void 대중교통검색() throws JsonProcessingException {
        GoogleDirectionReqDto directionReqDto = new GoogleDirectionReqDto();
        directionReqDto.setOrigin(좌표저장소.석계역.getCoordinate());
        directionReqDto.setDestination(좌표저장소.한성대학교.getCoordinate());
        GoogleDirectionResDto ret = googleAPIService.getDirection(directionReqDto);
        System.out.println("ret = " + ret.toString());
    }

    @Test
    public void 도보경로검색() throws JsonProcessingException {
        TMapDirectionReqDto tMapDirectionReqDto = new TMapDirectionReqDto();
        tMapDirectionReqDto.setStart(좌표저장소.석계역.getCoordinate());
        tMapDirectionReqDto.setEnd(좌표저장소.한성대학교.getCoordinate());
        TMapDirectionsResDto ret = tMapAPIService.getDirection(tMapDirectionReqDto);
        System.out.println("ret.toString() = " + ret.toString());

        List<LatLng> routes = new ArrayList<>();
        Steps res = DtoParseUtils.featureToSteps(ret.getFeatures().get(0));
        ret.getFeatures().forEach(i -> {
            routes.addAll(DtoParseUtils.extractLatLngInGeometry(i.getGeometry()));
        });
        res.setPolyline(new Polyline(PolylineEncoding.encode(routes)));
        System.out.println(res);
    }

    @Test
    public void 대중교통경로중걷는경로치환하기() throws JsonProcessingException {
        GoogleDirectionReqDto directionReqDto = new GoogleDirectionReqDto();
        directionReqDto.setOrigin(좌표저장소.석계역.getCoordinate());
        directionReqDto.setDestination(좌표저장소.마곡나루.getCoordinate());
        GoogleDirectionResDto ret = googleAPIService.getDirection(directionReqDto);
        ret = tMapAPIService.updateWalkingStepsInGoogleDirection(ret);
        System.out.println("ret = " + ret);
//        System.out.println("ret.getRoutes().get(0).getOverview_polyline().getPoints() = " + ret.getRoutes().get(0).getOverview_polyline().getPoints());
    }

    @Test
    public void TMap길찾기테스트() throws JsonProcessingException {
        TMapDirectionReqDto tMapDirectionReqDto = new TMapDirectionReqDto();
        tMapDirectionReqDto.setStart(좌표저장소.석계역.getCoordinate());
        tMapDirectionReqDto.setEnd(좌표저장소.마곡나루.getCoordinate());
        System.out.println(tMapAPIService.getDirection(tMapDirectionReqDto));
    }

    @Test
    public void TMap좌표가같으면() throws JsonProcessingException {
        TMapDirectionReqDto tMapDirectionReqDto = new TMapDirectionReqDto();
        tMapDirectionReqDto.setStart(좌표저장소.석계역.getCoordinate());
        tMapDirectionReqDto.setEnd(좌표저장소.석계역.getCoordinate());
        System.out.println(tMapAPIService.getDirection(tMapDirectionReqDto));
    }
}