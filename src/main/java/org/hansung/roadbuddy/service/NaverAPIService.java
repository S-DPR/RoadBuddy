package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hansung.roadbuddy.dto.naver.request.NaverGeocodingReqDto;
import org.hansung.roadbuddy.dto.naver.request.PlaceSearchItemReqDto;
import org.hansung.roadbuddy.dto.naver.response.PlaceSearchItem;
import org.hansung.roadbuddy.dto.naver.response.PlaceSearchResDto;
import org.hansung.roadbuddy.enums.HttpMethods;
import org.hansung.roadbuddy.generic.GenericAPIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
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
        List<String> words = Arrays.stream(naverGeocodingReqDto.getQuery().split(" ")).toList();
        for (int len = words.size(); len >= 1; len--) {
            for (int start = words.size()-len; start >= 0; start--) {
                String roadAddress = String.join(" ", words.subList(start, start + len));
                naverGeocodingReqDto.setQuery(roadAddress);
                try {
                    String response = sendRequest(geocodingEndpoint, HttpMethods.GET, createHttpRequestBuilder(), naverGeocodingReqDto);
                    Map ret = objectMapper.readValue(response, Map.class);
//                    System.out.println("((List)res.get(\"addresses\")).size()) = " + ((List)ret.get("addresses")).size());
                    if (((List)ret.get("addresses")).size() == 0) continue;
                    naverGeocodingReqDto.setQuery(String.join(" ", words));
                    cache.putIfAbsent(naverGeocodingReqDto, ret);
                    return ret;
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }

    public PlaceSearchResDto updatePlaceSearchItemsGeocoding(PlaceSearchResDto placeSearchResDto) {
        placeSearchResDto.getItems().forEach(x -> {
            PlaceSearchItemReqDto psi = (PlaceSearchItemReqDto) x;
            psi.setCoordinate(placeSearchResDto.getCoordinate());
            updatePlaceSearchItemReqDtoGeocoding(psi);
        });
        return placeSearchResDto;
    }

    public PlaceSearchItem updatePlaceSearchItemReqDtoGeocoding(PlaceSearchItemReqDto psi) {
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
