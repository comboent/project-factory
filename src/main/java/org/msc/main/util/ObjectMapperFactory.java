package org.msc.main.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class ObjectMapperFactory {
    private volatile static ObjectMapper mapper;

    public static ObjectMapper getInstance() {
        if(mapper == null) {
            synchronized (ObjectMapperFactory.class) {
                if(mapper == null) {
                    mapper = new ObjectMapper();
                    mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
                    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
                    mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
                    mapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
                        @Override
                        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                            jsonGenerator.writeString("");
                        }
                    });
                    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
                }
            }
        }
        return mapper;
    }
}
