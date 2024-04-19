package org.hansung.roadbuddy.restController;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hansung.roadbuddy.dto.google.request.AddressSearchReqDto;
import org.hansung.roadbuddy.dto.google.request.GoogleDirectionReqDto;
import org.hansung.roadbuddy.dto.google.request.GeocodingReqDto;
import org.hansung.roadbuddy.dto.google.request.TextSearchReqDto;
import org.hansung.roadbuddy.dto.google.response.googleDirections.GoogleDirectionResDto;
import org.hansung.roadbuddy.dto.naver.request.NaverGeocodingReqDto;
import org.hansung.roadbuddy.dto.naver.request.PlaceSearchReqDto;
import org.hansung.roadbuddy.dto.naver.response.PlaceSearchResDto;
import org.hansung.roadbuddy.generic.GenericRestController;
import org.hansung.roadbuddy.service.GoogleAPIService;
import org.hansung.roadbuddy.service.NaverAPIService;
import org.hansung.roadbuddy.service.NaverOpenAPIService;
import org.hansung.roadbuddy.service.TMapAPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/maps")
public class MapsRestController extends GenericRestController {
    private final GoogleAPIService googleAPIService;
    private final TMapAPIService tMapAPIService;
    private final NaverAPIService naverAPIService;
    private final NaverOpenAPIService naverOpenAPIService;
    MapsRestController(GoogleAPIService googleAPIService,
                       TMapAPIService tMapAPIService, NaverAPIService naverAPIService, NaverOpenAPIService naverOpenAPIService) {
        this.googleAPIService = googleAPIService;
        this.tMapAPIService = tMapAPIService;
        this.naverAPIService = naverAPIService;
        this.naverOpenAPIService = naverOpenAPIService;
    }
    @GetMapping("/old/geocoding")
    public ResponseEntity getGeocoding(GeocodingReqDto geocodingReqDto) throws JsonProcessingException {
        Map ret = googleAPIService.getAddressCoordinates(geocodingReqDto);
        return toResponse(ret);
    }

    @GetMapping("/old/locations")
    public ResponseEntity getSimilarAddress(AddressSearchReqDto addressSearchReqDto) throws JsonProcessingException {
        Map ret = googleAPIService.getSimilarAddressList(addressSearchReqDto);
        return toResponse(ret);
    }

    @GetMapping("/old/textSearch")
    public ResponseEntity getTextSearch(TextSearchReqDto textSearchReqDto) throws JsonProcessingException {
        Map ret = googleAPIService.getTextSearch(textSearchReqDto);
        return toResponse(ret);
    }

    @GetMapping("/locations")
    public ResponseEntity getLocations(PlaceSearchReqDto placeSearchReqDto) throws JsonProcessingException {
        PlaceSearchResDto ret = naverOpenAPIService.getPlaceSearch(placeSearchReqDto);
        ret = naverAPIService.updatePlaceSearchItemsGeocoding(ret);
        return toResponse(ret);
    }

    @GetMapping("/geocoding")
    public ResponseEntity getGeocoding(NaverGeocodingReqDto naverGeocodingReqDto) throws JsonProcessingException {
        Map ret = naverAPIService.getGeocoding(naverGeocodingReqDto);
        return toResponse(ret);
    }

    @GetMapping("/directions")
    public ResponseEntity getDirection(GoogleDirectionReqDto directionReqDto) throws JsonProcessingException {
        GoogleDirectionResDto googleDirectionResDto = googleAPIService.getDirection(directionReqDto);
        return toResponse(tMapAPIService.updateWalkingStepsInGoogleDirection(googleDirectionResDto));
    }
}
