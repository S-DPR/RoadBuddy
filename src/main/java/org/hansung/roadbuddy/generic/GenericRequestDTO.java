package org.hansung.roadbuddy.generic;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Setter
@Getter
@ToString
public abstract class GenericRequestDTO {
    private String apiKey;
    public abstract Map<String, String> toMap();
}
