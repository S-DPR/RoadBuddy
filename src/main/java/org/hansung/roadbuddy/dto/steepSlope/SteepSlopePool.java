package org.hansung.roadbuddy.dto.steepSlope;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.hansung.roadbuddy.generic.GenericPool;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Component
public class SteepSlopePool extends GenericPool<SubwaySleepSlopeDto, List<SteepSlope>> {
    protected SteepSlopePool(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @PostConstruct
    public void init() throws IOException {
        InputStream is = getInputStream("/subwaySteepSlope.json", "src/main/resources/subwaySteepSlope.json");
        List<SubwaySleepSlopeDto> dtos = objectMapper.readValue(is,
                objectMapper.getTypeFactory().constructCollectionType(List.class, SubwaySleepSlopeDto.class));
        dtos.forEach(i -> {
            pool.put(i, i.getSteepSlope());
        });
    }

    @Override
    public List<SteepSlope> get(SubwaySleepSlopeDto subwaySleepSlopeDto) {
        return pool.getOrDefault(subwaySleepSlopeDto, Collections.emptyList());
    }

    public List<SteepSlope> get(String line, String station) {
        return get(SubwaySleepSlopeDto.builder()
                .line(line)
                .station(station)
                .build());
    }
}
