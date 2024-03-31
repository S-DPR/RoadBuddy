package org.hansung.roadbuddy.generic;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class GenericRestController {
    protected <T> Map wrapServiceResponse(T reqDto, Function<T, Map> serviceMethod) {
        Map ret = new HashMap();
        ret.put("data", serviceMethod.apply(reqDto));
        return ret;
    }

    protected ResponseEntity toResponse(Object ret) {
        if (ret == null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity(ret, HttpStatus.OK);
    }
}
