package org.hansung.roadbuddy.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hansung.roadbuddy.dto.google.response.googleDirections.Legs;
import org.hansung.roadbuddy.dto.rail.response.RailTransferResDto;
import org.hansung.roadbuddy.generic.GenericRestController;
import org.hansung.roadbuddy.service.RailAPIService;
import org.hansung.roadbuddy.service.SteepSlopeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Route Extra Info API", description = "API for additional route information")
@RestController
@RequestMapping("/api/routes")
public class RouteExtraInfoAPI extends GenericRestController {

    private final RailAPIService railAPIService;
    private final SteepSlopeService steepSlopeService;

    RouteExtraInfoAPI(RailAPIService railAPIService, SteepSlopeService steepSlopeService) {
        this.railAPIService = railAPIService;
        this.steepSlopeService = steepSlopeService;
    }

    @Operation(summary = "지하철 엘리베이터 환승, 근처 급경사 정보 요청",
            description = "지하철 엘리베이터 환승 정보 및 급경사 정보를 동시에 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Legs.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/transferAndSteepSlope")
    public ResponseEntity getTransferAndSteepSlope(@RequestBody Legs legs) throws JsonProcessingException {
        Legs ret = railAPIService.updateRoutesSubwayTransfer(legs);
        ret = steepSlopeService.updateSteepSlopes(ret);
        return toResponse(ret);
    }

    @Operation(summary = "지하철 엘리베이터 환승 정보 요청", description = "지하철 엘리베이터 환승 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Legs.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/transfer")
    public ResponseEntity getTransfer(String line, String transferLine, String station) {
        return null; // 준비중
    }

    @Operation(summary = "지하철 근처 급경사지 정보 요청", description = "지하철 근처 급경사지 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Legs.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/steepSlope")
    public ResponseEntity getSteepSlope(String line, String station) {
        return null; // 준비중
    }
}
