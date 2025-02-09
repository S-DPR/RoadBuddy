package org.hansung.roadbuddy.dto.google.request;

import lombok.Getter;
import lombok.Setter;
import org.hansung.roadbuddy.generic.GenericRequestDTO;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class GeocodingReqDto extends GenericRequestDTO {
    private String address;
    private String language = "ko";

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key", getApiKey());
        map.put("address", address);
        map.put("language", language);
        return map;
    }
}
