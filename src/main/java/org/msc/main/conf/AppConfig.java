package org.msc.main.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AppConfig {
    @Value("#{app.uploadLimit}")
    private long uploadLimit;

    public long getUploadLimit() {
        return uploadLimit;
    }
}
