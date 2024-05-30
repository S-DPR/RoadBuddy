package org.hansung.roadbuddy.restController;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Hidden;
import org.hansung.roadbuddy.dto.google.response.googleDirections.Legs;
import org.hansung.roadbuddy.generic.GenericRestController;
import org.hansung.roadbuddy.service.RailAPIService;
import org.hansung.roadbuddy.service.SteepSlopeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subway")
@Hidden
public class RouteExtraInfoRestController extends GenericRestController {

    private final RailAPIService railAPIService;
    private final SteepSlopeService steepSlopeService;

    RouteExtraInfoRestController(RailAPIService railAPIService, SteepSlopeService steepSlopeService) {
        this.railAPIService = railAPIService;
        this.steepSlopeService = steepSlopeService;
    }

    @PostMapping("/transfer")
    public ResponseEntity getRailTransfer(@RequestBody Legs legs) throws JsonProcessingException {
        System.out.println("legs = " + legs);
        Legs ret = railAPIService.updateRoutesSubwayTransfer(legs);
        ret = steepSlopeService.updateSteepSlopes(ret);
        return toResponse(ret);
    }
}
