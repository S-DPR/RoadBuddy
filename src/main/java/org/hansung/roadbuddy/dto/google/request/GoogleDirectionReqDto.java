package org.hansung.roadbuddy.dto.google.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hansung.roadbuddy.dto.Coordinate;
import org.hansung.roadbuddy.generic.GenericRequestDTO;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@ToString
public class GoogleDirectionReqDto extends GenericRequestDTO {
    private Coordinate origin;
    private Coordinate destination;
    private String mode = "transit";
    private Boolean alternatives = true; // TODO TRUE로 해야함

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key", getApiKey());
        map.put("origin", origin.toGoogleString());
        map.put("destination", destination.toGoogleString());
        map.put("mode", mode);
        map.put("alternatives", alternatives.toString());
        return map;
    }
}
