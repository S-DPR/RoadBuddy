package org.hansung.roadbuddy.dto.taxi.request;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hansung.roadbuddy.generic.GenericRequestDTO;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class AverageReqDto extends GenericRequestDTO {
    private String sDate;
    private String eDate;

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key", getApiKey());
        map.put("sDate", sDate);
        map.put("eDate", eDate);
        return map;
    }
}
