package io.github.nnkwrik.common.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * @author nnkwrik
 * @date 18/11/19 16:54
 */
public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    public static TypeReference<Map<String, String>> simpleJsonMap =
            new TypeReference<Map<String, String>>(){};

    public static Map<String, String> fromJson(String rawData, TypeReference typeReference) {
        try {
            return mapper.readValue(rawData, typeReference);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String rawData, Class<T> classType) {
        try {
            return mapper.readValue(rawData, classType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
