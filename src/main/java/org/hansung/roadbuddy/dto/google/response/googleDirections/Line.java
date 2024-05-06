package org.hansung.roadbuddy.dto.google.response.googleDirections;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
public class Line {
    private List<Map> agencies;
    private String color;
    private String name;
    private String short_name;
    private String text_color;
    private Vehicle vehicle;
}
