package org.hansung.roadbuddy.enums;

import lombok.Getter;

@Getter
public enum GoogleDirectionMode {
    DRIVING("driving"),
    WALKING("walking"),
    BICYCLING("bicycling"),
    TRANSIT("transit");
    private final String mode;
    GoogleDirectionMode(String mode) {
        this.mode = mode;
    }
}
