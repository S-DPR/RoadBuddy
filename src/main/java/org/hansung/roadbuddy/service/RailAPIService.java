package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hansung.roadbuddy.dto.rail.request.RailTransferReqDto;
import org.hansung.roadbuddy.enums.HttpMethods;
import org.hansung.roadbuddy.generic.GenericAPIService;
import org.hansung.roadbuddy.generic.GenericRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RailAPIService extends GenericAPIService {

    private final String apiKey;

    private final String railEndpoint = "https://openapi.kric.go.kr/openapi/vulnerableUserInfo/transferMovement";

    @Autowired
    RailAPIService(ObjectMapper objectMapper, @Value("${api.key.rail}") String apiKey){
        super(objectMapper);
        this.apiKey = apiKey;
    }

    public Map getRailTransfer(RailTransferReqDto railTransferReqDto) throws JsonProcessingException {
        setKey(railTransferReqDto);
        return objectMapper.readValue(sendRequest(railEndpoint, HttpMethods.GET, railTransferReqDto), Map.class);
    }


    @Override
    protected void setKey(GenericRequestDTO source) {
        source.setApiKey(apiKey);
    }
}
