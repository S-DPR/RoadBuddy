package org.hansung.roadbuddy.restController;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.hansung.roadbuddy.dto.rail.request.RailTransferReqDto;
import org.hansung.roadbuddy.generic.GenericRestController;
import org.hansung.roadbuddy.service.RailAPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/subway")
public class RailRestController extends GenericRestController {

    private RailAPIService railAPIService;

    RailRestController(RailAPIService railAPIService) { this.railAPIService = railAPIService;}

    @GetMapping("/transfer")
    public ResponseEntity getRailTransfer(RailTransferReqDto railTransferReqDto) throws JsonProcessingException {
        Map ret = railAPIService.getRailTransfer(railTransferReqDto);
        return toResponse(ret);
    }

}
