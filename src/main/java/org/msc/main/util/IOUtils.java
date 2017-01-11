package org.msc.main.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class IOUtils {
    private static final Logger logger = LoggerFactory.getLogger(IOUtils.class);
    public static void transferTo(MultipartFile src, String destPath, String destFileName) {
        File dir = new File(destPath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        try {
            src.transferTo(new File(dir, destFileName));
        } catch (IOException e) {
            logger.error("transfer file failed!", e);
        }
    }
}
