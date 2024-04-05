package org.hansung.roadbuddy.dto.rail;

import lombok.Getter;
import lombok.Setter;
import org.hansung.roadbuddy.dto.Coordinate;
import org.hansung.roadbuddy.generic.GenericPostRequestDTO;

import java.util.Map;

@Setter
@Getter
public class RailDirectionReqDto extends GenericPostRequestDTO {

    private Coordinate start;
    private Coordinate end;

    public String getApiKeyDisplayName() {
        return "appKey";
    }

    @Override
    public Map<String, String> toMap() {
        return null;
    }
}
