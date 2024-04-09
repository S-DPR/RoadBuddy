package org.hansung.roadbuddy.api;

import org.hansung.roadbuddy.dto.rail.RailTransferReqDto;
import org.hansung.roadbuddy.service.RailAPIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class RailAPITest {

    @Autowired
    private RailAPIService railAPIService;

    @Test
    public void 환승이동경로검색(){
        RailTransferReqDto railTransferReqDto = new RailTransferReqDto();
        railTransferReqDto.setFormat("json");
        railTransferReqDto.setRailOprIsttCd("S1");
        railTransferReqDto.setLnCd("3");
        railTransferReqDto.setStinCd("321");
        railTransferReqDto.setChthTgtLn("4");
        railTransferReqDto.setPrevStinCd("422");
        railTransferReqDto.setChtnNextStinCd("424");
        Map ret = railAPIService.getRailTransfer(railTransferReqDto);
        ret.keySet().forEach(i -> {
            System.out.println( i + " " + ret.get(i));
        });
    }
}
