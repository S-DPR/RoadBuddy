package org.hansung.roadbuddy.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.hansung.roadbuddy.dto.taxi.request.AverageReqDto;
import org.hansung.roadbuddy.generic.GenericRestController;
import org.hansung.roadbuddy.service.TaxiAPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@Tag(name = "Taxi API", description = "API related to taxi services")
@RestController
@RequestMapping("/api/taxi")
public class TaxiAPI extends GenericRestController {
    private final TaxiAPIService taxiAPIService;

    public TaxiAPI(TaxiAPIService taxiAPIService) {
        this.taxiAPIService = taxiAPIService;
    }

    @Operation(summary = "택시 대기 시간 요청", description = "택시 호출 대기 시간을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = HashMap.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/waiting")
    public ResponseEntity getWaitingTime() throws JsonProcessingException {
        double totalMinutes = taxiAPIService.getTaxiCallWaiting(new AverageReqDto());
        int minutes = (int) totalMinutes;
        double fractionalPart = totalMinutes - minutes;
        int seconds = (int) (fractionalPart * 60);
        HashMap<String, String> res = new HashMap<>();
        res.put("time", minutes + "분 " + seconds + "초");
        return toResponse(res);
    }
}
