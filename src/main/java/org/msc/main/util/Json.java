package org.msc.main.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import net.sf.cglib.core.ReflectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class Json {
    private static final Logger logger = LoggerFactory.getLogger(Json.class);
    private static final ObjectMapper mapper = initObjectMapper();
    private static final String EMPTY_JSON_OBJ = "{}";

    private static ObjectMapper initObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
                jsonGenerator.writeString("");
            }
        });
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return mapper;
    }

    public static final String readJsonFile(String file) {
        InputStream is = Json.class.getClassLoader().getResourceAsStream(file);
        try {
            JsonNode node = mapper.readTree(is);
            String json = node.toString();
            return json;
        } catch (IOException e) {
            logger.error("can not open file " + file);
            return EMPTY_JSON_OBJ;
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
            return EMPTY_JSON_OBJ;
        }
        try {
            return mapper.writeValueAsString(src);
        } catch (JsonProcessingException e) {
            logger.error("to json meet an exception : " + e.getMessage());
        }
        return EMPTY_JSON_OBJ;
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
