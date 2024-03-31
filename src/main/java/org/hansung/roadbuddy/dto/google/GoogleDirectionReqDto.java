package org.hansung.roadbuddy.dto.google;

import lombok.Getter;
import lombok.Setter;
import org.hansung.roadbuddy.dto.Coordinate;
import org.hansung.roadbuddy.generic.GenericRequestDTO;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public class GoogleDirectionReqDto extends GenericRequestDTO {
    private Coordinate origin;
    private Coordinate destination;
    private String mode = "TRANSIT";
    private Boolean alternatives = true;

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
