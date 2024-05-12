package org.hansung.roadbuddy.dto.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.hansung.roadbuddy.dto.google.response.googleDirections.Steps;
import org.hansung.roadbuddy.generic.GenericPool;
import org.hansung.roadbuddy.utilService.DtoGetUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class SubwayInfoPool extends GenericPool<SubwayInfo, SubwayInfo> {
    SubwayInfoPool(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @PostConstruct
    public void init() throws IOException {
        initPool();
        initConnect();
    }

    private void initPool() throws IOException {
        InputStream is = getInputStream("/subwayCode.json", "src/main/resources/subwayCode.json");
        List<SubwayInfo> subwayInfos = objectMapper.readValue(is,
                objectMapper.getTypeFactory().constructCollectionType(List.class, SubwayInfo.class));
        subwayInfos.forEach(i -> {
            i.setConnect(new ArrayList<>());
            pool.put(i, i);
        });
    }

    private void initConnect() throws IOException {
        InputStream is = getInputStream("/subwayConnectionInfo.json", "src/main/resources/subwayConnectionInfo.json");
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
    }

    public SubwayInfo get(String line, String station) {
        return get(SubwayInfo.builder()
                .line(line)
                .station(station)
                .build());
    }

    public SubwayInfo getByStepWithArrival(Steps step) {
        String station = DtoGetUtils.getArrivalStop(step);
        String line = DtoGetUtils.getLineShortName(step);
        return get(line, station);
    }

    public SubwayInfo getByStepWithDeparture(Steps step) {
        String station = DtoGetUtils.getDepartureStop(step);
        String line = DtoGetUtils.getLineShortName(step);
        return get(line, station);
    }
}
