package org.msc.main.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MsgUtils {
    private static final Logger logger = LoggerFactory.getLogger(MsgUtils.class);

    private static final Map<String, MessageFormat> formats = new HashMap<String, MessageFormat>();

    static {
        logger.info("init msg.properties");
        try {
            Properties p = new Properties();
            p.load(MsgUtils.class.getClassLoader().getResourceAsStream("msg.properties"));
            for (String propName : p.stringPropertyNames()) {
                String template = p.getProperty(propName);
                if (!StringUtils.isEmpty(template)) {
                    MessageFormat format = new MessageFormat(template);
                    formats.put(propName, format);
                } else {
                    logger.error("msg.properties contains empty key : " + propName);
                }
            }
            logger.info("init msg.properties OK");
        } catch (IOException e) {
            logger.error("init msg.properties ERROR", e);
        }
    }

    public static String getMsg(String key, Object... overrides) {
        if (!formats.containsKey(key)) {
            return "";
        }
        MessageFormat format = formats.get(key);
        return format.format(overrides);
    }
}
