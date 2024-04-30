package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hansung.roadbuddy.dto.naver.request.PlaceSearchReqDto;
import org.hansung.roadbuddy.dto.naver.response.PlaceSearchResDto;
import org.hansung.roadbuddy.enums.HttpMethods;
import org.hansung.roadbuddy.generic.GenericAPIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;

@Service
public class NaverOpenAPIService extends GenericAPIService {
    private final String clientId;
    private final String secretKey;
    private final String placeSearchEndpoint = "https://openapi.naver.com/v1/search/local.json";
    protected NaverOpenAPIService(ObjectMapper objectMapper,
                                  @Value("${api.key.naver.open.clientId}") String clientId,
                                  @Value("${api.key.naver.open.secret}") String secretKey) {
        super(objectMapper, null);
        this.clientId = clientId;
        this.secretKey = secretKey;
    }

    @Override
    protected HttpRequest.Builder createHttpRequestBuilder() {
        return HttpRequest.newBuilder()
                .header("X-Naver-Client-Id", clientId)
                .header("X-Naver-Client-Secret", secretKey);
    }

    public PlaceSearchResDto getPlaceSearch(PlaceSearchReqDto placeSearchReqDto) throws JsonProcessingException {
        if (cache.containsKey(placeSearchReqDto)) return (PlaceSearchResDto) cache.get(placeSearchReqDto);
        String response = sendRequest(placeSearchEndpoint, HttpMethods.GET, createHttpRequestBuilder(), placeSearchReqDto);
        System.out.println("placesearch response = " + response);
        PlaceSearchResDto placeSearchResDto = objectMapper.readValue(response, PlaceSearchResDto.class);
        placeSearchResDto.setCoordinate(placeSearchReqDto.getCoordinate());
        cache.putIfAbsent(placeSearchReqDto, placeSearchResDto);
        return placeSearchResDto;
    }
}
