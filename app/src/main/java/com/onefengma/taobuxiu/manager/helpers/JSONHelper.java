package com.onefengma.taobuxiu.manager.helpers;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @author yfchu
 * @date 2016/8/12
 */
public class JSONHelper {

    public static String toJson(Object object) {
        return JSON.toJSONString(object, SerializerFeature.DisableCheckSpecialChar);
    }

    public static <T> T parse(String jsonStr, Class<T> tClass) {
        return JSON.parseObject(jsonStr, tClass);
    }

    public static <T> T to(JsonNode jsonNode, Class<T> tClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonNode.textValue(), tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
