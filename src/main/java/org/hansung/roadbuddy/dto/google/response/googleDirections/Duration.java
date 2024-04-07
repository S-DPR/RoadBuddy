package org.hansung.roadbuddy.dto.google.response.googleDirections;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Duration {
    private String text;
    private Long value;

    public Duration(long value) {
        setValueAndText(value);
    }

    public void setValueAndText(long value) {
        this.value = value;
        long min = (value + 59) / 60;
        long hour = min / 60;
        min = min % 60;

        if (hour > 0) {
            this.text = String.format("%d시간\n%d분", hour, min);
        } else {
            this.text = String.format("%d분", min);
        }
    }
}
