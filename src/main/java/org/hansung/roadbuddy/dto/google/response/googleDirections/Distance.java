package org.hansung.roadbuddy.dto.google.response.googleDirections;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Distance {
    private String text;
    private Long value;

    public Distance(long value) {
        setValueAndText(value);
    }

    public void setValueAndText(long value) {
        this.value = value;
        if (value > 1000) {
            this.text = String.format("%.2fkm", (double)value/1000.0);
        } else {
            this.text = String.format("%dm", value);
        }
    }
}
