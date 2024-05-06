package org.hansung.roadbuddy.restController;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hansung.roadbuddy.dto.google.response.googleDirections.Legs;
import org.hansung.roadbuddy.generic.GenericRestController;
import org.hansung.roadbuddy.service.RailAPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subway")
public class RailRestController extends GenericRestController {

    private RailAPIService railAPIService;

    RailRestController(RailAPIService railAPIService) { this.railAPIService = railAPIService;}

    @PostMapping("/transfer")
    public ResponseEntity getRailTransfer(@RequestBody Legs legs) throws JsonProcessingException {
        System.out.println("legs = " + legs);
        Legs ret = railAPIService.updateRoutesSubwayTransfer(legs);
        return toResponse(ret);
    }
}
