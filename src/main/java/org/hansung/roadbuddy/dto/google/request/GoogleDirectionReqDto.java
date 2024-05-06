package org.hansung.roadbuddy.dto.google.request;

import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = false)
public class GoogleDirectionReqDto extends GenericRequestDTO {
    private Coordinate origin;
    private Coordinate destination;
    private String mode = "transit";
    private Boolean alternatives = true; // TODO TRUE로 해야함
    private String language = "ko";
    private String departure_time = "now";

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("key", getApiKey());
        map.put("origin", origin.toGoogleString());
        map.put("destination", destination.toGoogleString());
        map.put("mode", mode);
        map.put("alternatives", alternatives.toString());
        map.put("language", language);
        map.put("departure_time", departure_time);
//        map.put("departure_time", "1715380424");
        return map;
    }
}
