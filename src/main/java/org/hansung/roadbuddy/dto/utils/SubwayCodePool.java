package org.hansung.roadbuddy.dto.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.hansung.roadbuddy.generic.GenericPool;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.List;

@Component
public class SubwayCodePool extends GenericPool<SubwayInfo, SubwayCode> {
    SubwayCodePool(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @PostConstruct
    public void init() throws IOException {
        InputStream is = getInputStream("/subwayCode.json", "src/main/resources/subwayCode.json");
        List<SubwayInfo> subwayInfos = objectMapper.readValue(is,
                objectMapper.getTypeFactory().constructCollectionType(List.class, SubwayInfo.class));

        is = getInputStream("/subwayCode.json", "src/main/resources/subwayCode.json");
        List<SubwayCode> subwayCodes = objectMapper.readValue(is,
                objectMapper.getTypeFactory().constructCollectionType(List.class, SubwayCode.class));

        if (subwayCodes.size() != subwayInfos.size()) System.out.println("ERROR");
        for (int i = 0; i < subwayInfos.size(); i++) {
            SubwayInfo subwayInfo = subwayInfos.get(i);
            SubwayCode subwayCode = subwayCodes.get(i);
            if (pool.containsKey(subwayInfo)) System.out.println("subwayInfo = " + subwayInfo);
            pool.put(subwayInfo, subwayCode);
        }
    }

    public SubwayCode get(String line, String station) {
        return get(SubwayInfo.builder()
                .line(line)
                .station(station)
                .build());
    }
}
