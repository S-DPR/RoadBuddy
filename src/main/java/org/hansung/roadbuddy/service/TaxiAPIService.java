package org.hansung.roadbuddy.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hansung.roadbuddy.dto.taxi.request.AverageReqDto;
import org.hansung.roadbuddy.dto.taxi.response.AverageResDto;
import org.hansung.roadbuddy.generic.GenericAPIService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class TaxiAPIService extends GenericAPIService {
    private final String averageEndPoint = "https://m.calltaxi.sisul.or.kr/api/open/newJson0001.asp";

    protected TaxiAPIService(ObjectMapper objectMapper, @Value("${api.key.callTaxi}") String apiKey) {
        super(objectMapper, apiKey);
    }

    public double getTaxiCallWaiting(AverageReqDto averageReqDto) throws JsonProcessingException {
        setKey(averageReqDto);
        setDate(averageReqDto);
        if (cache.containsKey(averageReqDto)) return (double) cache.get(averageReqDto);
        String res = sendRequest(averageEndPoint, averageReqDto);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(res);
        JsonNode listNode = rootNode.path("data").path("list");
        System.out.println("listNode.toString() = " + listNode.toString());
        List<AverageResDto> averageResDtos = objectMapper.readValue(listNode.toString(), new TypeReference<List<AverageResDto>>(){});
        double ret = averageCalculate(averageResDtos);
        cache.put(averageReqDto, ret);
        return ret;
    }

    private void setDate(AverageReqDto averageReqDto) {
        LocalDate currentDate = LocalDate.now();
        LocalDate threeMonthsAgo = currentDate.minusMonths(3);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String threeMonthsAgoStr = threeMonthsAgo.format(formatter);
        String currentStr = currentDate.format(formatter);
        averageReqDto.setSDate(threeMonthsAgoStr);
        averageReqDto.setEDate(currentStr);
    }

    private double averageCalculate(List<AverageResDto> averageResDtos) {
        LocalDate currentDate = LocalDate.now();
        DayOfWeek currentWeek = currentDate.getDayOfWeek();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return averageResDtos.stream()
                .mapToDouble(i -> {
                    LocalDate date = LocalDate.parse(i.getRunDate(), formatter);
                    DayOfWeek dayOfWeek = date.getDayOfWeek();
                    return currentWeek == dayOfWeek ? i.getPickupTime() : Double.NaN;
                })
                .filter(value -> !Double.isNaN(value))
                .average()
                .orElse(0.0);
    }
}
