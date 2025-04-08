package org.hansung.roadbuddy.dto.google.request;

import lombok.Getter;
import lombok.Setter;
import org.hansung.roadbuddy.generic.GenericRequestDTO;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class AddressSearchReqDto extends GenericRequestDTO {
    private String input;
    private String language = "ko";
    private String type = "geocode";

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key", getApiKey());
        map.put("input", input);
        map.put("type", type);
        return map;
    }
}
