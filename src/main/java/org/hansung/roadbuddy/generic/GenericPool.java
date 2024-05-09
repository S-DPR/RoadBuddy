package org.hansung.roadbuddy.generic;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.HashMap;

public abstract class GenericPool<K, V> {
    protected final ObjectMapper objectMapper;
    protected final HashMap<K, V> pool;

    protected GenericPool(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.pool = new HashMap<>();
    }

    protected InputStream getInputStream(String path, String failPath) throws FileNotFoundException {
        InputStream is = getClass().getResourceAsStream(path);
        if (is != null) return is;
        // 리소스 스트림이 null이면 파일 시스템에서 찾기 시도
        File file = new File(failPath);
        if (file.exists()) return new FileInputStream(file);
        throw new FileNotFoundException("Resource file not found. Path " + path);
    }

    public V get(K key) {
        return pool.get(key);
    }
}
