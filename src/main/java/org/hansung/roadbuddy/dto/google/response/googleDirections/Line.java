package org.hansung.roadbuddy.dto.google.response.googleDirections;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Line {
    private Map agencies;
    private String color;
    private String name;
    private String short_name;
    private String text_color;
    private Vehicle vehicle;
}
