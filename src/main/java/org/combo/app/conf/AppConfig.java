package org.combo.app.conf;

import net.sf.cglib.reflect.FastClass;
import org.combo.app.exception.resolver.ExceptionResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

@Component
public class AppConfig {

    private static final Logger logger = LoggerFactory.getLogger(AppConfig.class);
    private Map<String, FastClass> exceptionResolverCache = new HashMap<String, FastClass>();

    @Value("#{app.uploadLimit}")
    private long uploadLimit;

    @Value("#{app.uploadRootPath}")
    private String uploadRootPath;

    @Value("#{app.exceptionResolverImpl}")
    private String exceptionResolverImpl;

    @Value("#{app.hashAlgorithmName}")
    private String hashAlgorithmName;

    @Value("#{app.hashIterations}")
    private int hashIterations;

    @Value("#{app.pwdRetryLimit}")
    private int pwdRetryLimit;

    public long getUploadLimit() {
        return uploadLimit;
    }

    public String getUploadRootPath() {
        return uploadRootPath;
    }

    public String getHashAlgorithmName() {
        return hashAlgorithmName;
    }

    public int getHashIterations() {
        return hashIterations;
    }

    public ExceptionResolver getExceptionResolver() {
        try {
            FastClass fastClass;
            if(exceptionResolverCache.containsKey(exceptionResolverImpl)) {
                fastClass = exceptionResolverCache.get(exceptionResolverImpl);
            } else {
                fastClass = FastClass.create(Class.forName(exceptionResolverImpl));
                exceptionResolverCache.put(exceptionResolverImpl, fastClass);
            }
            return (ExceptionResolver) fastClass.newInstance();
        } catch (InvocationTargetException e) {
            logger.error("can not get ExceptionResolver", e);
        } catch (ClassNotFoundException e) {
            logger.error("can not found class " + exceptionResolverImpl, e);
        }
        return null;
    }

    public int getPwdRetryLimit() {
        return pwdRetryLimit;
    }
}
