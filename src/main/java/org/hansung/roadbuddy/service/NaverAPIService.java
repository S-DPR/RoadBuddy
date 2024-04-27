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
        String response = sendRequest(geocodingEndpoint, HttpMethods.GET, createHttpRequestBuilder(), naverGeocodingReqDto);
        System.out.println("response = " + response);
        return objectMapper.readValue(response, Map.class);
    }

    public PlaceSearchResDto updatePlaceSearchItemsGeocoding(PlaceSearchResDto placeSearchResDto) throws JsonProcessingException {
        for (PlaceSearchItem x: placeSearchResDto.getItems()) {
            NaverGeocodingReqDto naverGeocodingReqDto = new NaverGeocodingReqDto();
            naverGeocodingReqDto.setQuery(x.getAddress());
            naverGeocodingReqDto.setCoordinate(placeSearchResDto.getCoordinate());
            x.setGeocoding(getGeocoding(naverGeocodingReqDto));
        }
        return placeSearchResDto;
    }
}
