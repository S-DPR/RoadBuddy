package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hansung.roadbuddy.dto.naver.request.NaverGeocodingReqDto;
import org.hansung.roadbuddy.dto.naver.response.PlaceSearchItem;
import org.hansung.roadbuddy.dto.naver.response.PlaceSearchResDto;
import org.hansung.roadbuddy.enums.HttpMethods;
import org.hansung.roadbuddy.generic.GenericAPIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class NaverAPIService extends GenericAPIService {
    private final String clientId;
    private final String secretKey;
    private final String geocodingEndpoint = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode";
    protected NaverAPIService(ObjectMapper objectMapper,
                                  @Value("${api.key.naver.clientId}") String clientId,
                                  @Value("${api.key.naver.secret}") String secretKey) {
        super(objectMapper, null);
        this.clientId = clientId;
        this.secretKey = secretKey;
    }

    @Override
    protected HttpRequest.Builder createHttpRequestBuilder() {
        return HttpRequest.newBuilder()
                .header("X-NCP-APIGW-API-KEY-ID", clientId)
                .header("X-NCP-APIGW-API-KEY", secretKey);
    }

    public Map getGeocoding(NaverGeocodingReqDto naverGeocodingReqDto) throws JsonProcessingException {
        if (cache.containsKey(naverGeocodingReqDto)) return (Map) cache.get(naverGeocodingReqDto);
        // 만약 주어진 주소가 A B C D E라면,
        // 'A B C D E'를 모두 쿼리에 넣으면 결과가 나오지 않지만
        // 'C D E'처럼 일부만 넣을 경우 결과가 나오는 경우가 있다.
        // 이를 해결하기 위해 길이를 기본으로 가능한 모든 방법을 넣기로 한다.
        String originQuery = naverGeocodingReqDto.getQuery();
        List<String> combination = getAddressCombination(originQuery);
        for (String query: combination) {
            try {
                naverGeocodingReqDto.setQuery(query);
                String response = sendRequest(geocodingEndpoint, HttpMethods.GET, createHttpRequestBuilder(), naverGeocodingReqDto);
                Map ret = objectMapper.readValue(response, Map.class);
                naverGeocodingReqDto.setQuery(originQuery);
                cache.putIfAbsent(naverGeocodingReqDto, ret);
                return ret;
            } catch (JsonProcessingException ignored) {
            }
        }
        return null;
    }

    private List<String> getAddressCombination(String query) {
        List<String> ret = new ArrayList<>();
        List<String> words = Arrays.stream(query.split(" ")).toList();
        for (int len = words.size(); len >= 1; len--) {
            for (int start = words.size()-len; start >= 0; start--) {
                String roadAddress = String.join(" ", words.subList(start, start + len));
                if (!roadAddress.isEmpty()) ret.add(roadAddress);
            }
        }
        return ret;
    }

    public PlaceSearchResDto updatePlaceSearchItemsGeocoding(PlaceSearchResDto placeSearchResDto) {
        placeSearchResDto.getItems().forEach(x -> {
            x.setCoordinate(placeSearchResDto.getCoordinate());
            updatePlaceSearchItemReqDtoGeocoding(x);
        });
        return placeSearchResDto;
    }

    public PlaceSearchItem updatePlaceSearchItemReqDtoGeocoding(PlaceSearchItem psi) {
        NaverGeocodingReqDto naverGeocodingReqDto = new NaverGeocodingReqDto();
        naverGeocodingReqDto.setQuery(psi.getRoadAddress().isEmpty() ? psi.getAddress() : psi.getRoadAddress());
        naverGeocodingReqDto.setCoordinate(psi.getCoordinate());
        try {
            psi.setGeocoding(getGeocoding(naverGeocodingReqDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return psi;
    }
}
