package org.hansung.roadbuddy.dto.rail.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SubwayInfoPool {
    private final ObjectMapper objectMapper;
    private final HashMap<SubwayInfo, SubwayInfo> pool;

    SubwayInfoPool(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        pool = new HashMap<>();
    }

    @PostConstruct
    public void init() {
        initPool();
        initConnect();
    }

    private void initPool() {
        try {
            String path = "/subwayCode.json";
            InputStream is = getClass().getResourceAsStream(path); // 파일 대신 InputStream 사용
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
            subwayInfos.forEach(i -> {
                i.setConnect(new ArrayList<>());
                pool.put(i, i);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initConnect() {
        try {
            String path = "/subwayConnectionInfo.json";
            InputStream is = getClass().getResourceAsStream(path);
            if (is == null) {
                // 리소스 스트림이 null이면 파일 시스템에서 찾기 시도
                File file = new File("src/main/resources/subwayConnectionInfo.json");
                if (file.exists()) {
                    is = new FileInputStream(file);
                } else {
                    throw new FileNotFoundException("Resource file not found. Path " + path);
                }
            }
            List<Map<String, String>> subwayInfos = objectMapper.readValue(is,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
            subwayInfos.forEach(i -> {
                String station = i.get("station");
                String line = i.get("line");
                String connect = i.get("connect");

                SubwayInfo cur = get(line, station);
                SubwayInfo nxt = get(line, connect);

                cur.getConnect().add(nxt);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SubwayInfo get(SubwayInfo subwayInfo) {
        return pool.get(subwayInfo);
    }

    public SubwayInfo get(String line, String station) {
        return get(SubwayInfo.builder()
                .line(line)
                .station(station)
                .build());
    }
}
