package com.shengjing.ibd.scheduler.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by royrliu on 2016/8/12.
 */
public class JacksonUtils {
    private static Logger log = Logger.getLogger(JacksonUtils.class);

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//        mapper.enableDefaultTyping();
    }

    public static <T> T parse(Class<T> c, String json) {

        try {
            T obj = mapper.readValue(json, c);
            return obj;
        } catch (Exception e) {
            log.error("JacksonUtil parse error", e);
            return null;
        }

    }

    public static <T, E> T parse(Class<T> clsContent, Class<E> clsElement, String json) {
        try {
            T ret = null;
            if (clsContent.equals(List.class)) {
                JavaType javaType = getCollectionType(ArrayList.class, clsElement);
                ret = mapper.readValue(json, javaType);
            }
            return ret;
        } catch (Exception e) {
            log.error("JacksonUtil parse error", e);
            return null;
        }

    }

    public static String toJson(Object obj) {
        try {
            String val = mapper.writeValueAsString(obj);
            return val;
        } catch (Exception e) {
            log.error("JacksonUtil toJson error", e);
            return null;
        }

    }

    /**
     * 获取泛型的Collection Type
     *
     * @param collectionClass 泛型的Collection
     * @param elementClasses  元素类
     * @return JavaType Java类型
     * @since 1.0
     */
    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }
}
