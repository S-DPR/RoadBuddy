package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hansung.roadbuddy.dto.google.response.googleDirections.Legs;
import org.hansung.roadbuddy.dto.google.response.googleDirections.Steps;
import org.hansung.roadbuddy.dto.rail.request.RailTransferReqDto;
import org.hansung.roadbuddy.dto.rail.response.RailTransferResDto;
import org.hansung.roadbuddy.dto.rail.utils.SubwayInfo;
import org.hansung.roadbuddy.generic.GenericAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RailAPIService extends GenericAPIService {

    private final String railEndpoint = "https://openapi.kric.go.kr/openapi/vulnerableUserInfo/transferMovement";
    private final Map<SubwayInfo, String> code = new HashMap<>();

    @Autowired
    RailAPIService(ObjectMapper objectMapper, @Value("${api.key.rail}") String apiKey){
        super(objectMapper, apiKey);
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/java/org/hansung/roadbuddy/resource/subwayCode.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                List<String> items = Arrays.stream(line.split(", ")).toList();
                SubwayInfo subwayInfo = new SubwayInfo();
                subwayInfo.setLine(items.get(0));
                subwayInfo.setStation(items.get(2));
                code.put(subwayInfo, items.get(1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public RailTransferResDto getRailTransfer(RailTransferReqDto railTransferReqDto) throws JsonProcessingException {
        setKey(railTransferReqDto);
        return objectMapper.readValue(sendRequest(railEndpoint, railTransferReqDto), RailTransferResDto.class);
    }

    public void updateRoutesSubwayTransfer(Legs legs) throws JsonProcessingException {
        List<Steps> steps = legs.getSteps();
        int len = steps.size();
        for (int i = 0; i < len-1; i++) {
            if (!isSubway(steps.get(i))) continue;
            if (!isSubway(steps.get(i+1))) continue;
            Steps first = steps.get(i);
            Steps second = steps.get(i+1);
            String fromStation = (String) first.getTransit_details().getArrival_stop().get("name");
            String fromLine = (String) first.getTransit_details().getLine().getShort_name();
            String toStation = (String) second.getTransit_details().getDeparture_stop().get("name");
        }
        for (int i = 0; i < len-2; i++) {
            if (!isSubway(steps.get(i))) continue;
            if (!isWalking(steps.get(i+1))) continue;
            if (!isSubway(steps.get(i+2))) continue;
            Steps first = steps.get(i);
            Steps second = steps.get(i+2);
            String from = (String) first.getTransit_details().getArrival_stop().get("name");
            String to = (String) second.getTransit_details().getDeparture_stop().get("name");
        }
    }

    private boolean isSubway(Steps step) {
        if (!step.getTravel_mode().equals("TRANSMIT")) return false;
        return step.getTransit_details().getLine().getVehicle().getType().equals("SUBWAY");
    }

    private boolean isWalking(Steps step) {
        return step.getTravel_mode().equals("WALKING");
    }
    // 만들 함수에서 해야할거
    // 1. Steps를 쭉 보면서 (Steps 내부 Steps는 볼 필요 없음)
    // 2. WALKING의 목적이 환승인 경우를 찾아냄 (솔직히 어떻게해야할지 잘 모름 생각해봐야함)
    // 3. 너가 만든 RailTransferReqDto를 만든 다음 getRailTransfer 메소드에 넣어줌 (제대로 넣었다면 결과가 나올거임)
    // 4. DirectionsTransferResDto에다가 추가변수 몇개 선언해서 3번에서 나온 응답을 반환해주면 됨
    // + 3번에서 Map을 쓸거면 Key로는 환승역과 도착선을 알 수 있게 해주는게 나을 것 같음
}
