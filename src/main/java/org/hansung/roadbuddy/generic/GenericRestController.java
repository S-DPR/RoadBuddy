package org.hansung.roadbuddy.generic;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public abstract class GenericRestController {
    protected ResponseEntity toResponse(Object data) {
        if (data == null) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        Map ret = new HashMap();
        ret.put("data", data);
        return new ResponseEntity(ret, HttpStatus.OK);
    }
}
