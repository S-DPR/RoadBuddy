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

import static org.hansung.roadbuddy.utilService.DtoGetUtils.*;

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
        for (int i = 0; i < len-2; i++) {
            if (!isTransferSteps(steps, i)) continue;

            // 환승 역 및 호선 추적
            SubwayInfo transferFrom = subwayInfoPool.getByStepWithArrival(steps.get(i));
            SubwayInfo transferTo = subwayInfoPool.getByStepWithDeparture(steps.get(i+2));

            // 환승 관련 코드 얻기
            SubwayCode cFrom = subwayCodePool.get(transferFrom);
            SubwayCode cTo = subwayCodePool.get(transferTo);

            // 환승으로 도달하는 최종 도착지와 거리
            SubwayInfo arrivalStation = subwayInfoPool.getByStepWithArrival(steps.get(i+2));
            int dist = getArrivalDist(steps.get(i+2));

            // 어느 방향으로 가야 그 도착지가 나오는지
            List<SubwayInfo> toNxt = transferTo.findDistKStationPath(arrivalStation, dist, new HashSet<>());

            List<RailTransferResDto<List<String>>> path = updateTransferPath(transferTo, toNxt, cFrom, cTo);

            // 방향 필터링
            path = filterOppositeDirection(path, steps.get(i));

            // i+1번째가 환승할때 필요한 경로이므로 이 부분에 환승 업데이트
            steps.get(i + 1).setTransfer_path(path);
        }
        return legs;
    }

    private List<RailTransferResDto<List<String>>> updateTransferPath(
            SubwayInfo transferTo,
            List<SubwayInfo> toNxt,
            SubwayCode cFrom,
            SubwayCode cTo) throws JsonProcessingException {
        List<RailTransferResDto<List<String>>> ret = new ArrayList<>();
        for (SubwayInfo prv: transferTo.getConnect()) {
            // 첫번째 조건 : 이게 끝 역인지 (리프노드는 간선이 1개)
            // 두번째 조건 : 현재 prv가 이전역이 아닌 가야하는 방향의 역인지
            if (transferTo.getConnect().size() > 1 && toNxt.contains(prv)) continue;

            // 현재 역이 이전역이면 목적지로 가는 모든 역에 대해서 쿼리
            for (SubwayInfo nxt: toNxt) {
                RailTransferReqDto railTransferReqDto = RailTransferReqDto.builder()
                        .railOprIsttCd(cFrom.getRailCd())
                        .lnCd(cFrom.getLnCd())
                        .stinCd(cFrom.getStationCd())
                        .prevStinCd(subwayCodePool.get(prv).getStationCd())
                        .chthTgtLn(cTo.getLnCd())
                        .chtnNextStinCd(subwayCodePool.get(nxt).getStationCd())
                        .build();

                // 경로 결과 받기
                TypeReference<List<RailTransferResDto<String>>> typeRef = new TypeReference<>() {};
                ArrayList body = (ArrayList) getRailTransfer(railTransferReqDto).getOrDefault("body", new ArrayList<>());
                List<RailTransferResDto<String>> items = objectMapper.convertValue(body, typeRef);

                // 받은 경로 결과를 클라이언트가 사용하기 쉽게 압축
                List<RailTransferResDto<List<String>>> combine = combineMvContDtl(items);
                ret.addAll(combine);
            }
        }
        return ret;
    }

    private List<RailTransferResDto<List<String>>> combineMvContDtl(List<RailTransferResDto<String>> items) {
        Map<String, RailTransferResDto<List<String>>> combine = new HashMap<>();
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
        return combine.values().stream().toList();
    }

    private List<RailTransferResDto<List<String>>> filterOppositeDirection(
            List<RailTransferResDto<List<String>>> res,
            Steps steps) {
        SubwayInfo start = subwayInfoPool.getByStepWithDeparture(steps);
        SubwayInfo end = subwayInfoPool.getByStepWithArrival(steps);
        SubwayInfo oppositeDirection = start.findArrivalNextStation(end, null);
        List<RailTransferResDto<List<String>>> ret = res.stream().filter(i -> {
            return i.getStMovePath().contains(oppositeDirection.getStation());
        }).toList();
        return ret.isEmpty() ? res : ret;
    }

    private boolean isTransferSteps(List<Steps> steps, int idx) {
        if (!isSubway(steps.get(idx))) return false;
        if (!isWalking(steps.get(idx+1))) return false;
        if (!isSubway(steps.get(idx+2))) return false;
        return true;
    }

    private int getArrivalDist(Steps steps) {
        String num = steps.getTransit_details().getNum_stops();
        if (num != null) {
            return Integer.parseInt(num);
        }
        String start = getDepartureStop(steps);
        String end = getArrivalStop(steps);
        String line = getLineShortName(steps);
        SubwayInfo startStation = subwayInfoPool.get(line, start);
        SubwayInfo destination = subwayInfoPool.get(line, end);
        return startStation.findStationShortestDistance(destination);
    }
}
