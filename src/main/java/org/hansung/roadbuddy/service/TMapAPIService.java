package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hansung.roadbuddy.dto.tmap.TMapDirectionReqDto;
import org.hansung.roadbuddy.enums.HttpMethods;
import org.hansung.roadbuddy.generic.GenericAPIService;
import org.hansung.roadbuddy.generic.GenericRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TMapAPIService extends GenericAPIService {
    private final String apiKey;
    private final String tMapDirectionEndpoint = "https://apis.openapi.sk.com/tmap/routes/pedestrian?version=1&callback=function";

    @Autowired
    TMapAPIService(ObjectMapper objectMapper, @Value("${api.key.tmap}") String apiKey) {
        super(objectMapper);
        this.apiKey = apiKey;
    }

    public Map getDirection(TMapDirectionReqDto tMapCoordinate) {
        setKey(tMapCoordinate);
        return sendRequest(tMapDirectionEndpoint, HttpMethods.POST, tMapCoordinate);
    }

    @Override
    protected void setKey(GenericRequestDTO dto) {
        dto.setApiKey(apiKey);
    }
}
