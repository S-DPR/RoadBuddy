//package org.hansung.roadbuddy.restController;
//
//import org.hansung.roadbuddy.dto.roadBuddy.request.EscalatorReqDto;
//import org.hansung.roadbuddy.generic.GenericRestController;
//import org.hansung.roadbuddy.service.EscalatorService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/info")
//public class SubwayInfoRestController extends GenericRestController {
//    private final EscalatorService escalatorService;
//
//    public SubwayInfoRestController(EscalatorService escalatorService) {
//        this.escalatorService = escalatorService;
//    }
//
//    @GetMapping("/escalator")
//    public ResponseEntity getEscalators(EscalatorReqDto dto) {
//        List ret = escalatorService.findByLineName(dto);
//        return toResponse(ret);
//    }
//}
