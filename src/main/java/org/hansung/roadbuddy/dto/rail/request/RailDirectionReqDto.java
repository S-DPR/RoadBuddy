package org.hansung.roadbuddy.dto.rail.request;

import lombok.Getter;
import lombok.Setter;
import org.hansung.roadbuddy.dto.Coordinate;
import org.hansung.roadbuddy.generic.GenericRequestDTO;

import java.util.Map;

@Setter
@Getter
public class RailDirectionReqDto extends GenericRequestDTO {

    private Coordinate start;
    private Coordinate end;

    @Override
    public Map<String, String> toMap() {
        return null;
    }
}
