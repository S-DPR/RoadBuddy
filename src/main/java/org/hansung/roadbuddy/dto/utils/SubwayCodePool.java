package org.hansung.roadbuddy.dto.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.List;

@Component
public class SubwayCodePool {
    private final ObjectMapper objectMapper;
    private HashMap<SubwayInfo, SubwayCode> pool;

    SubwayCodePool(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        pool = new HashMap<>();
        try {
            String path = "/subwayCode.json";
            InputStream is = getClass().getResourceAsStream(path);
            if (is == null) {
                // 리소스 스트림이 null이면 파일 시스템에서 찾기 시도
                File file = new File("src/main/resources/subwayCode.json");
                if (file.exists()) {
                    is = new FileInputStream(file);
                } else {
                    throw new FileNotFoundException("Resource file not found. Path " + path);
                }
            }
            List<SubwayInfo> subwayInfos = objectMapper.readValue(is,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, SubwayInfo.class));

            is = getClass().getResourceAsStream(path);
            if (is == null) {
                // 리소스 스트림이 null이면 파일 시스템에서 찾기 시도
                File file = new File("src/main/resources/subwayCode.json");
                if (file.exists()) {
                    is = new FileInputStream(file);
                } else {
                    throw new FileNotFoundException("Resource file not found. Path " + path);
                }
            }
            List<SubwayCode> subwayCodes = objectMapper.readValue(is,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, SubwayCode.class));

            if (subwayCodes.size() != subwayInfos.size()) System.out.println("ERROR");
            for (int i = 0; i < subwayInfos.size(); i++) {
                SubwayInfo subwayInfo = subwayInfos.get(i);
                SubwayCode subwayCode = subwayCodes.get(i);
                if (pool.containsKey(subwayInfo)) System.out.println("subwayInfo = " + subwayInfo);
                pool.put(subwayInfo, subwayCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SubwayCode get(SubwayInfo subwayInfo) {
        return pool.get(subwayInfo);
    }

    public SubwayCode get(String line, String station) {
        return get(SubwayInfo.builder()
                .line(line)
                .station(station)
                .build());
    }
}
