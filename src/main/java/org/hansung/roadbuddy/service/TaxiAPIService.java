package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hansung.roadbuddy.generic.GenericAPIService;
import org.springframework.beans.factory.annotation.Value;

public class TaxiAPIService extends GenericAPIService {

    protected TaxiAPIService(ObjectMapper objectMapper, @Value("api.key.taxi") String apiKey) {
        super(objectMapper, apiKey);
    }
}
