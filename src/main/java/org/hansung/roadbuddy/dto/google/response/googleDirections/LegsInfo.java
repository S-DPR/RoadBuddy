package org.hansung.roadbuddy.dto.google.response.googleDirections;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

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

    public void setValueAndText(long time) {
        value = time;
        ZoneId zone = ZoneId.of("Asia/Seoul");
        LocalDateTime updatedTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(value), zone);
        if (updatedTime.getSecond() > 0) {
            updatedTime = updatedTime.truncatedTo(ChronoUnit.MINUTES).plusMinutes(1);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("a h:mm").withZone(zone);
        text = formatter.format(updatedTime).replace("AM", "오전").replace("PM", "오후");
    }
}
