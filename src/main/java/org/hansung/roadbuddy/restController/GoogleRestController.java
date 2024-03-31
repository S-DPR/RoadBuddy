package org.hansung.roadbuddy.restController;

import org.hansung.roadbuddy.dto.google.AddressSearchReqDto;
import org.hansung.roadbuddy.dto.google.GoogleDirectionReqDto;
import org.hansung.roadbuddy.dto.google.GeocodingReqDto;
import org.hansung.roadbuddy.generic.GenericRestController;
import org.hansung.roadbuddy.service.GoogleAPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/maps")
public class GoogleRestController extends GenericRestController {
    private GoogleAPIService googleAPIService;
    GoogleRestController(GoogleAPIService googleAPIService) {
        this.googleAPIService = googleAPIService;
    }
    @GetMapping("/geocoding")
    public ResponseEntity getGeocoding(GeocodingReqDto geocodingReqDto) {
        Map ret = wrapServiceResponse(geocodingReqDto, googleAPIService::getAddressCoordinates);
        return toResponse(ret);
    }

    @GetMapping("/locations")
    public ResponseEntity getSimilarAddress(AddressSearchReqDto addressSearchReqDto) {
        Map ret = wrapServiceResponse(addressSearchReqDto, googleAPIService::getSimilarAddressList);
        return toResponse(ret);
    }

    @GetMapping("/directions")
    public ResponseEntity getDirection(GoogleDirectionReqDto directionReqDto) {
        Map ret = wrapServiceResponse(directionReqDto, googleAPIService::getDirection);
        return toResponse(ret);
    }
}
