package org.hansung.roadbuddy.dto.google.response.googleDirections;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LegsInfo {
    private String text;
    private String time_zone;
    private Long value;
}
