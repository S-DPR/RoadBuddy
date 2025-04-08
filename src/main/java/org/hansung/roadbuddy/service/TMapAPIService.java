package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hansung.roadbuddy.dto.tmap.response.tmapDirections.TMapDirectionsResDto;
import org.hansung.roadbuddy.dto.tmap.request.TMapDirectionReqDto;
import org.hansung.roadbuddy.enums.HttpMethods;
import org.hansung.roadbuddy.generic.GenericAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.http.HttpRequest;
import java.util.Map;

@Service
public class TMapAPIService extends GenericAPIService {
    private final String apiKey;
    private final String tMapDirectionEndpoint = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1&callback=function";
    private final String tMapDriveDirectionEndpoint = "https://apis.openapi.sk.com/tmap/routes?version=1";

    @Autowired
    TMapAPIService(ObjectMapper objectMapper, @Value("${api.key.tmap}") String apiKey) {
        super(objectMapper, null);
        this.apiKey = apiKey;
    }

    @Override
    protected HttpRequest.Builder createHttpRequestBuilder() {
        return HttpRequest.newBuilder()
                .header("appKey", apiKey);
    }

    public TMapDirectionsResDto getDirection(TMapDirectionReqDto tMapDirectionReqDto) throws JsonProcessingException {
        setKey(tMapDirectionReqDto);
        String response = sendRequest(tMapDirectionEndpoint, HttpMethods.POST, createHttpRequestBuilder(), tMapDirectionReqDto).replaceAll("\\p{Cntrl}", "");
        TMapDirectionsResDto ret = objectMapper.readValue(response, TMapDirectionsResDto.class);
        return ret;
    }

    public Map getDriveDirection(TMapDirectionReqDto tMapDirectionReqDto) throws JsonProcessingException {
        setKey(tMapDirectionReqDto);
        tMapDirectionReqDto.setSearchOption(0L);
        String response = sendRequest(tMapDriveDirectionEndpoint, HttpMethods.POST, createHttpRequestBuilder(), tMapDirectionReqDto);
        Map ret = objectMapper.readValue(response, Map.class);
        return ret;
    }
}
