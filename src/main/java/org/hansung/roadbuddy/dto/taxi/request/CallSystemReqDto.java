package org.hansung.roadbuddy.dto.taxi.request;

import lombok.Getter;
import lombok.Setter;
import org.hansung.roadbuddy.generic.GenericRequestDTO;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class CallSystemReqDto extends GenericRequestDTO {
    private String type = "xml";
    private String service = "disabledCalltaxi";
    private Integer start_index; // 요청시작위치
    private Integer end_index; // 요청종료위치
    private String reg_date; // 요청일

    // 이놈 쿼리파라미터가 아니라 endpoint에 url 만들어서 넣어줘야함
    @Override
    public Map<String, String> toMap() {
        return new HashMap<>();
    }
}
