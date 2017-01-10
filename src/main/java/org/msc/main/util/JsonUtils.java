package org.msc.main.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.cglib.core.ReflectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);
    private static final ObjectMapper mapper = ObjectMapperFactory.getInstance();

    public static final String readJsonFile(String file) {
        InputStream is = JsonUtils.class.getClassLoader().getResourceAsStream(file);
        try {
            JsonNode node = mapper.readTree(is);
            String json = node.toString();
            return json;
        } catch (IOException e) {
            logger.error("can not open file " + file);
            return "";
        }
    }

    public static final <E> List<E> readJsonFileToList(String file, Class<E> clazz) {
        String json = readJsonFile(file);
        return fromJsonToList(json, clazz);
    }

    public static final <K, V> List<Map<K, V>> readJsonFileToListWithMapElement(String file) {
        String json = readJsonFile(file);
        return fromJsonToListWithMapElement(json);
    }

    public static final String toJson(Object src) {
        if (src == null) {
            return "";
        }
        try {
            return mapper.writeValueAsString(src);
        } catch (JsonProcessingException e) {
            logger.error("to json meet an exception : " + e.getMessage());
        }
        return "";
    }

    public static final <T> T fromJson(String json, Class<T> clazz) {
        if(StringUtils.isEmpty(json)) {
            return null;
        }
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            logger.error("from json meet an exception : " + e.getMessage());
        }
        return clazz.cast(ReflectUtils.newInstance(clazz));
    }

    public static final <K, V> Map<K, V> fromJsonToMap(String json) {
        if(StringUtils.isEmpty(json)) {
            return new HashMap<K, V>();
        }
        try {
            return mapper.readValue(json, new TypeReference<Map<K, V>>() {
            });
        } catch (IOException e) {
            logger.error("from json meet an exception : " + e.getMessage());
        }
        return new HashMap<K, V>();
    }

    public static final <E> List<E> fromJsonToList(String json, Class<E> clazz) {
        List<E> result = new ArrayList<E>();
        if(StringUtils.isEmpty(json)) {
            return result;
        }
        try {
            JsonNode jsonNode = mapper.readTree(json);
            Iterator<JsonNode> it = jsonNode.elements();
            while (it.hasNext()) {
                JsonNode next = it.next();
                E e;
                if(next.isObject()) {
                    e = fromJson(next.toString(), clazz);
                } else {
                    e = mapper.readValue(next.toString(), clazz);
                }
                result.add(e);
            }
            return result;
        } catch (IOException e) {
            logger.error("from json to list meet an exception : " + e.getMessage());
        }
        return result;
    }

    public static final <K, V> List<Map<K, V>> fromJsonToListWithMapElement(String json) {
        List<Map<K, V>> result = new ArrayList<Map<K, V>>();
        if(StringUtils.isEmpty(json)) {
            return result;
        }
        try {
            JsonNode jsonNode = mapper.readTree(json);
            Iterator<JsonNode> it = jsonNode.elements();
            while (it.hasNext()) {
                JsonNode next = it.next();
                Map<K, V> map = fromJsonToMap(next.toString());
                result.add(map);
            }
            return result;
        } catch (IOException e) {
            logger.error("from json to list with map element meet an exception : " + e.getMessage());
        }
        return result;
    }
}
