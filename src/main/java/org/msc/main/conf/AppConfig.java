package org.msc.main.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {

    @Value("#{app.uploadLimit}")
    private long uploadLimit;

    @Value("#{app.uploadRootPath}")
    private String uploadRootPath;

    public long getUploadLimit() {
        return uploadLimit;
    }

    public String getUploadRootPath() {
        return uploadRootPath;
    }
}
