package org.hansung.roadbuddy.restController;

import org.hansung.roadbuddy.dto.rail.RailTransferReqDto;
import org.hansung.roadbuddy.generic.GenericRestController;
import org.hansung.roadbuddy.service.RailAPIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/maps")
public class RailRestController extends GenericRestController {

    private RailAPIService railAPIService;

    RailRestController(RailAPIService railAPIService) { this.railAPIService = railAPIService;}

    @GetMapping("/railtransfer")
    public ResponseEntity getRailTransfer(RailTransferReqDto railTransferReqDto){
        Map ret = wrapServiceResponse(railTransferReqDto, railAPIService::getRailTransfer);
        return toResponse(ret);
    }

}
