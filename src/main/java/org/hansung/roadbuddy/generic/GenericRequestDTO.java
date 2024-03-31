package org.hansung.roadbuddy.generic;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public abstract class GenericRequestDTO {
    private String apiKey;
    public abstract Map<String, String> toMap();
}
