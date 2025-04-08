package org.hansung.roadbuddy.deserialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.List;

public class TMapCoordinatesDeserializer extends JsonDeserializer<List<List<Double>>> {
    @Override
    public List<List<Double>> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {// JsonParser를 사용하여 JSON 데이터를 직접 읽어 List<List<Double>> 형태로 변환합니다.
        JsonNode node = p.getCodec().readTree(p);
        List result = p.getCodec().treeToValue(node, List.class);
        if (!(result.get(0) instanceof List)) {
            result = List.of(result);
        }
        return result;
    }
}