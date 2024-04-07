package org.hansung.roadbuddy.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hansung.roadbuddy.service.GoogleAPIService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name="GeoCoding API", description = "API related to GeoCoding")
@RestController
@RequestMapping("/maps")
@CrossOrigin
public class GoogleAPI {
    private GoogleAPIService googleAPIService;
    GoogleAPI(GoogleAPIService googleAPIService) {
        this.googleAPIService = googleAPIService;
    }

//    @Operation(summary = "주소로 좌표 검색", description = "주어진 주소에 대한 위도와 경도 좌표를 반환합니다.")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "주소에 대한 좌표 반환 성공",
//                    content = @Content(schema = @Schema(implementation = Map.class))),
//            @ApiResponse(responseCode = "203", description = "요청은 성공했지만 결과를 찾을 수 없음"),
//            @ApiResponse(responseCode = "500", description = "서버 내부 오류"),
//    })
//    @GetMapping(value = "/geoCoding/{address}")
//    public GenericResponse<?> getCoordinationByAddress(@PathVariable String address) {
//        try {
//            Map data = googleService.getAddressCoordination(address);
//            List ret = (List) data.get("results");
//            String status = (String) data.get("status");
//            if (status.equals("OK")) {
//                return new GenericResponse<>(200, HttpStatus.OK, ret.size(), ret);
//            }
//            return new GenericResponse<>(203, HttpStatus.NO_CONTENT, data.size(), data);
//        } catch (Exception e) {
//            return new GenericResponse<>(500, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}
