package org.hansung.roadbuddy.dto.google.request;

import lombok.Getter;
import lombok.Setter;
import org.hansung.roadbuddy.generic.GenericRequestDTO;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class TextSearchReqDto extends GenericRequestDTO {
    private String language = "ko";
    private String query;

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key", getApiKey());
        map.put("query", query);
        map.put("language", language);
        return map;
    }
}
