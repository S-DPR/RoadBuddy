package org.hansung.roadbuddy.restController;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hansung.roadbuddy.dto.taxi.request.AverageReqDto;
import org.hansung.roadbuddy.generic.GenericRestController;
import org.hansung.roadbuddy.service.TaxiAPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/taxi")
public class TaxiRestController extends GenericRestController {
    private final TaxiAPIService taxiAPIService;

    public TaxiRestController(TaxiAPIService taxiAPIService) {
        this.taxiAPIService = taxiAPIService;
    }

    @GetMapping("/waiting")
    public ResponseEntity getWaitingTime() throws JsonProcessingException {
        double totalMinutes = taxiAPIService.getTaxiCallWaiting(new AverageReqDto());
        int minutes = (int) totalMinutes; // 정수 부분을 분으로
        double fractionalPart = totalMinutes - minutes; // 소수 부분을 따로 분리
        int seconds = (int) (fractionalPart * 60); // 소수 부분을 초로 변환
        HashMap<String, String> res = new HashMap<>();
        res.put("time", minutes + "분 " + seconds + "초");
        return toResponse(res);
    }
}
