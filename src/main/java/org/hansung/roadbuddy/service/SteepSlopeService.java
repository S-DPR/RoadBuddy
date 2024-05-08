package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hansung.roadbuddy.dto.SteepSlopeDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SteepSlopeService {
    private final HashMap<String, List<SteepSlopeDto>> map = new HashMap<>();

    public SteepSlopeService(@Value("${steepSlopeFilePath}") String filePath) {
        initializeMapFromFile(filePath); // 파일에서 데이터를 읽어와서 map 초기화
    }

    // 파일에서 데이터를 읽어와서 map을 초기화하는 메서드
    private void initializeMapFromFile(String filePath) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            StringBuilder jsonBuilder = new StringBuilder();
            String readLine;
            while ((readLine = br.readLine()) != null) {
                jsonBuilder.append(readLine);
            }
            br.close();

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> dataList = objectMapper.readValue(jsonBuilder.toString(), new TypeReference<List<Map<String, Object>>>() {});
            for (Map<String, Object> data : dataList) {
                String line = (String) data.get("line");
                String station = (String) data.get("name");
                List<Map<String, Object>> steepSlopeList = (List<Map<String, Object>>) data.get("steepSlope");
                List<SteepSlopeDto> steepSlopes = new ArrayList<>();
                for (Map<String, Object> steepSlopeData : steepSlopeList) {
                    String address = (String) steepSlopeData.get("address");
                    String shortAddress = (String) steepSlopeData.get("shortAddress");
                    Double latitude = (Double) steepSlopeData.get("latitude");
                    Double longitude = (Double) steepSlopeData.get("longitude");
                    SteepSlopeDto steepSlopeDto = new SteepSlopeDto(address, shortAddress, latitude, longitude);
                    steepSlopes.add(steepSlopeDto);
                }
                String key = line + " " + station;
                map.put(key, steepSlopes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // line과 station에 해당하는 가파른 경사 데이터를 반환하는 메서드
    public List<SteepSlopeDto> getSteepSlopeBySubway(String line, String station) {
        String key = line + " " + station;
        return map.getOrDefault(key, new ArrayList<>());
    }
}
