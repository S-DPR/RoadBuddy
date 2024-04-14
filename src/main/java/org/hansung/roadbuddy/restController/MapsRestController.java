package org.hansung.roadbuddy.restController;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hansung.roadbuddy.dto.google.request.AddressSearchReqDto;
import org.hansung.roadbuddy.dto.google.request.GoogleDirectionReqDto;
import org.hansung.roadbuddy.dto.google.request.GeocodingReqDto;
import org.hansung.roadbuddy.dto.google.response.googleDirections.GoogleDirectionResDto;
import org.hansung.roadbuddy.generic.GenericRestController;
import org.hansung.roadbuddy.service.GoogleAPIService;
import org.hansung.roadbuddy.service.TMapAPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/maps")
public class MapsRestController extends GenericRestController {
    private GoogleAPIService googleAPIService;
    private TMapAPIService tMapAPIService;
    MapsRestController(GoogleAPIService googleAPIService,
                       TMapAPIService tMapAPIService) {
        this.googleAPIService = googleAPIService;
        this.tMapAPIService = tMapAPIService;
    }
    @GetMapping("/geocoding")
    public ResponseEntity getGeocoding(GeocodingReqDto geocodingReqDto) throws JsonProcessingException {
        Map ret = googleAPIService.getAddressCoordinates(geocodingReqDto);
        return toResponse(ret);
    }

    @GetMapping("/locations")
    public ResponseEntity getSimilarAddress(AddressSearchReqDto addressSearchReqDto) throws JsonProcessingException {
        Map ret = googleAPIService.getSimilarAddressList(addressSearchReqDto);
        return toResponse(ret);
    }

    @GetMapping("/directions")
    public ResponseEntity getDirection(GoogleDirectionReqDto directionReqDto) throws JsonProcessingException {
        GoogleDirectionResDto googleDirectionResDto = googleAPIService.getDirection(directionReqDto);
        return toResponse(tMapAPIService.updateWalkingStepsInGoogleDirection(googleDirectionResDto));
    }
}
