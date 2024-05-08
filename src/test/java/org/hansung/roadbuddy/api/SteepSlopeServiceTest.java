package org.hansung.roadbuddy.api;

import org.hansung.roadbuddy.dto.SteepSlopeDto;
import org.hansung.roadbuddy.service.SteepSlopeService;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SteepSlopeServiceTest {
    private static final String FILE_PATH = "src/main/java/org/hansung/roadbuddy/resource/subwaySteepSlope.json";

    @Test
    public void 경사도체크() {
        SteepSlopeService steepSlopeService = new SteepSlopeService(FILE_PATH);

        String line = "03호선";
        String station = "금호";
        // getSteepSlopeBySubway 메서드 호출
        List<SteepSlopeDto> steepSlopes = steepSlopeService.getSteepSlopeBySubway(line, station);

        // 결과 출력
        if (steepSlopes != null) {
            for (SteepSlopeDto steepSlope : steepSlopes) {
                System.out.println("Address: " + steepSlope.getAddress());
                System.out.println("Short Address: " + steepSlope.getShortAddress());
                System.out.println("Coordinate: [" + steepSlope.getLatitude() + "," + steepSlope.getLongitude() + "]");
                System.out.println();
            }
        } else {
            System.out.println("No steep slopes found for the specified subway station.");
        }
    }
}
