package org.hansung.roadbuddy.dto.rail.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hansung.roadbuddy.generic.GenericRequestDTO;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Builder
public class RailTransferReqDto extends GenericRequestDTO {

    //쿼리 파라미터
    private String format = "json";
    private String railOprIsttCd; //철도운영기관코드
    private String lnCd; //환승 이전 호선
    private String stinCd; //환승할 역 코드
    private String prevStinCd; //환승 이전역 코드
    private String chthTgtLn; //환승대상선
    private String chtnNextStinCd; //환승 이후역 코드

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("serviceKey", getApiKey());
        map.put("format", "json");
        map.put("railOprIsttCd", railOprIsttCd);
        map.put("lnCd", lnCd);
        map.put("stinCd", stinCd);
        map.put("prevStinCd", prevStinCd);
        map.put("chthTgtLn", chthTgtLn);
        map.put("chtnNextStinCd", chtnNextStinCd);
        return map;
    }
}
