package org.hansung.roadbuddy.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hansung.roadbuddy.dto.google.request.AddressSearchReqDto;
import org.hansung.roadbuddy.dto.google.request.GoogleDirectionReqDto;
import org.hansung.roadbuddy.dto.google.request.GeocodingReqDto;
import org.hansung.roadbuddy.dto.google.request.TextSearchReqDto;
import org.hansung.roadbuddy.dto.google.response.googleDirections.GoogleDirectionResDto;
import org.hansung.roadbuddy.dto.naver.request.NaverGeocodingReqDto;
import org.hansung.roadbuddy.dto.naver.request.PlaceSearchReqDto;
import org.hansung.roadbuddy.dto.naver.response.PlaceSearchItem;
import org.hansung.roadbuddy.dto.naver.response.PlaceSearchResDto;
import org.hansung.roadbuddy.dto.tmap.request.TMapDirectionReqDto;
import org.hansung.roadbuddy.generic.GenericRestController;
import org.hansung.roadbuddy.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Maps API", description = "API related to various map services")
@RestController
@RequestMapping("/api/maps")
public class MapsAPI extends GenericRestController {
    private final GoogleAPIService googleAPIService;
    private final TMapAPIService tMapAPIService;
    private final NaverAPIService naverAPIService;
    private final NaverOpenAPIService naverOpenAPIService;

    MapsAPI(GoogleAPIService googleAPIService,
                       TMapAPIService tMapAPIService, NaverAPIService naverAPIService, NaverOpenAPIService naverOpenAPIService) {
        this.googleAPIService = googleAPIService;
        this.tMapAPIService = tMapAPIService;
        this.naverAPIService = naverAPIService;
        this.naverOpenAPIService = naverOpenAPIService;
    }

    @Operation(summary = "Geocoding 요청", description = "주소 정보를 통해 위도와 경도 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/old/geocoding")
    public ResponseEntity getGeocoding(GeocodingReqDto geocodingReqDto) throws JsonProcessingException {
        Map ret = googleAPIService.getAddressCoordinates(geocodingReqDto);
        return toResponse(ret);
    }

    @Operation(summary = "주소 검색 요청", description = "주소 정보를 통해 유사한 주소 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/old/locations")
    public ResponseEntity getSimilarAddress(AddressSearchReqDto addressSearchReqDto) throws JsonProcessingException {
        Map ret = googleAPIService.getSimilarAddressList(addressSearchReqDto);
        return toResponse(ret);
    }

    @Operation(summary = "텍스트 검색 요청", description = "텍스트 정보를 통해 관련 장소 목록을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/old/textSearch")
    public ResponseEntity getTextSearch(TextSearchReqDto textSearchReqDto) throws JsonProcessingException {
        Map ret = googleAPIService.getTextSearch(textSearchReqDto);
        return toResponse(ret);
    }

    @Operation(summary = "장소 검색 요청", description = "장소 검색 요청에 대한 결과를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PlaceSearchResDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/locations")
    public ResponseEntity getLocations(PlaceSearchReqDto placeSearchReqDto) throws JsonProcessingException {
        PlaceSearchResDto ret = naverOpenAPIService.getPlaceSearch(placeSearchReqDto);
        if (placeSearchReqDto.isIncludeGeocoding()) {
            ret = naverAPIService.updatePlaceSearchItemsGeocoding(ret);
        }
        return toResponse(ret);
    }

    @Operation(summary = "Naver Geocoding 요청", description = "Naver API를 통해 Geocoding 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/geocoding")
    public ResponseEntity getGeocoding(NaverGeocodingReqDto naverGeocodingReqDto) throws JsonProcessingException {
        Map ret = naverAPIService.getGeocoding(naverGeocodingReqDto);
        return toResponse(ret);
    }

    @Operation(summary = "장소에 대한 Geocoding 요청", description = "장소 정보를 통해 Geocoding 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = PlaceSearchItem.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/geocoding/byPlace")
    public ResponseEntity getGeocoding(PlaceSearchItem placeSearchItem) throws JsonProcessingException {
        PlaceSearchItem ret = naverAPIService.updatePlaceSearchItemReqDtoGeocoding(placeSearchItem);
        return toResponse(ret);
    }

    @Operation(summary = "경로 요청", description = "경로 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = GoogleDirectionResDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/directions")
    public ResponseEntity getDirection(GoogleDirectionReqDto directionReqDto) throws JsonProcessingException {
        try {
            GoogleDirectionResDto googleDirectionResDto = googleAPIService.getDirection(directionReqDto);
            return toResponse(tMapAPIService.updateWalkingStepsInGoogleDirection(googleDirectionResDto));
        } catch (NullPointerException e) {
            return new ResponseEntity(Map.of("status", "무료 서비스 사용량 부족 의심"), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @Operation(summary = "자동차 경로 요청", description = "자동차 경로 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/drive")
    public ResponseEntity getDriveDirection(TMapDirectionReqDto tMapDirectionReqDto) throws JsonProcessingException {
        Map ret = tMapAPIService.getDriveDirection(tMapDirectionReqDto);
        return toResponse(ret);
    }
}
