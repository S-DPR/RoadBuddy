package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hansung.roadbuddy.dto.google.response.googleDirections.Legs;
import org.hansung.roadbuddy.dto.google.response.googleDirections.Steps;
import org.hansung.roadbuddy.dto.rail.request.RailTransferReqDto;
import org.hansung.roadbuddy.dto.rail.response.RailTransferResDto;
import org.hansung.roadbuddy.dto.utils.SubwayCode;
import org.hansung.roadbuddy.dto.utils.SubwayCodePool;
import org.hansung.roadbuddy.dto.utils.SubwayInfo;
import org.hansung.roadbuddy.dto.utils.SubwayInfoPool;
import org.hansung.roadbuddy.generic.GenericAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RailAPIService extends GenericAPIService {

    private final String railEndpoint = "https://openapi.kric.go.kr/openapi/vulnerableUserInfo/transferMovement";
    private final SubwayInfoPool subwayInfoPool;
    private final SubwayCodePool subwayCodePool;

    @Autowired
    RailAPIService(ObjectMapper objectMapper, @Value("${api.key.rail}") String apiKey, SubwayInfoPool subwayInfoPool, SubwayCodePool subwayCodePool){
        super(objectMapper, apiKey);
        this.subwayInfoPool = subwayInfoPool;
        this.subwayCodePool = subwayCodePool;
    }

    public Map getRailTransfer(RailTransferReqDto railTransferReqDto) throws JsonProcessingException {
        setKey(railTransferReqDto);
        return objectMapper.readValue(sendRequest(railEndpoint, railTransferReqDto), Map.class);
    }

    public Legs updateRoutesSubwayTransfer(Legs legs) throws JsonProcessingException {
        List<Steps> steps = legs.getSteps();
        int len = steps.size();
//        for (int i = 0; i < len-1; i++) {
//            if (!isSubway(steps.get(i))) continue;
//            if (!isSubway(steps.get(i+1))) continue;
//            SubwayCode from = getStationCodeBySteps(steps.get(i));
//            SubwayCode to = getStationCodeBySteps(steps.get(i+1));
////            steps.add(i+1, );
//        }
//        private String railOprIsttCd; //철도운영기관코드
//        private String lnCd; //환승 이전 호선
//        private String stinCd; //환승할 역 코드
//        private String prevStinCd; //환승 이전역 코드
//        private String chthTgtLn; //환승대상선
//        private String chtnNextStinCd; //환승 이후역 코드
        for (int i = 0; i < len-2; i++) {
            if (!isSubway(steps.get(i))) continue;
            if (!isWalking(steps.get(i+1))) continue;
            if (!isSubway(steps.get(i+2))) continue;
            SubwayInfo from = getSubwayInfoBySteps(steps.get(i));
            String nxtStation = from.getStation();
            String nxtLine = steps.get(i+2).getTransit_details().getLine().getShort_name();
            SubwayInfo to = subwayInfoPool.get(nxtLine, nxtStation);
            SubwayCode cFrom = subwayCodePool.get(from);
            SubwayCode cTo = subwayCodePool.get(to);
            String arrivalStation = (String) steps.get(i+2).getTransit_details().getDeparture_stop().get("name");
            int dist = getArrivalDist(steps.get(i+2));
            List<SubwayInfo> toNxt = to.findDistKStationPath(arrivalStation, dist, new HashSet<>());
            for (SubwayInfo prv: to.getConnect()) {
                if (to.getConnect().size() > 1 && toNxt.contains(prv)) continue;
                for (SubwayInfo nxt: toNxt) {
                    RailTransferReqDto railTransferReqDto = RailTransferReqDto.builder()
                            .railOprIsttCd(cFrom.getRailCd())
                            .lnCd(cFrom.getLnCd())
                            .stinCd(cFrom.getStationCd())
                            .prevStinCd(subwayCodePool.get(prv).getStationCd())
                            .chthTgtLn(cTo.getLnCd())
                            .chtnNextStinCd(subwayCodePool.get(nxt).getStationCd())
                            .build();
                    TypeReference<List<RailTransferResDto<String>>> typeRef = new TypeReference<>() {};
                    ArrayList body = (ArrayList) getRailTransfer(railTransferReqDto).getOrDefault("body", new ArrayList<>());
                    List<RailTransferResDto<String>> items = objectMapper.convertValue(body, typeRef);
                    Map<String, RailTransferResDto<List<String>>> combine = new HashMap<>();
                    System.out.println("railTransferRes = " + items.size());
                    items.forEach(item -> {
                        String id = item.getImgPath();
                        combine.putIfAbsent(id, RailTransferResDto.<List<String>>builder()
                                .chtnMvTpOrdr(item.getChtnMvTpOrdr())
                                .stMovePath(item.getStMovePath())
                                .edMovePath(item.getEdMovePath())
                                .elvtSttCd(item.getElvtSttCd())
                                .elvtTpCd(item.getElvtTpCd())
                                .imgPath(item.getImgPath())
                                .mvContDtl(new ArrayList<>())
                                .mvPathMgNo(item.getMvPathMgNo())
                                .build());
                        combine.get(id).getMvContDtl().add(item.getMvContDtl());
                    });
                    System.out.println("combine = " + combine.size());
                    List<RailTransferResDto<List<String>>> ret = combine.values().stream().toList();
                    System.out.println("railTransferResDto = " + ret);
                    steps.get(i+1).setTransfer_path(ret);
                }
            }
        }
        return legs;
    }

    private boolean isSubway(Steps step) {
        if (!step.getTravel_mode().equals("TRANSIT")) return false;
        return step.getTransit_details().getLine().getVehicle().getType().equals("SUBWAY");
    }

    private boolean isWalking(Steps step) {
        return step.getTravel_mode().equals("WALKING");
    }

    private String getArrivalStop(Steps steps) {
        return (String) steps.getTransit_details().getArrival_stop().get("name");
    }

    private int getArrivalDist(Steps steps) {
        return Integer.parseInt(steps.getTransit_details().getNum_stops());
    }

    private SubwayInfo getSubwayInfoBySteps(Steps steps) {
        String station = getArrivalStop(steps);
        String line = steps.getTransit_details().getLine().getShort_name();
        return subwayInfoPool.get(line, station);
    }
}
