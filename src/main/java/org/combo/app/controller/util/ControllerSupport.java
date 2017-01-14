package org.combo.app.controller.util;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.combo.app.net.Response;
import org.combo.app.net.ResponseFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControllerSupport implements ApplicationContextAware, ServletContextAware {

    private static final Logger logger = LoggerFactory.getLogger(ControllerSupport.class);

    protected ApplicationContext applicationContext;

    protected ServletContext servletContext;

    private Map<String, FastMethod> methodCache = new ConcurrentHashMap<String, FastMethod>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 查找Service中的方法， 方法不能重载
     * @param bean
     * @param method
     * @param params
     * @param api
     * @return
     */
    protected FastMethod findMethod(Object bean, String method, List<Object> params, String api) {
        Class<?> clazz = bean.getClass();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if(declaredMethod.getName().equals(method) && declaredMethod.getParameterTypes().length == params.size()) {
                FastClass fastClass = FastClass.create(clazz);
                return fastClass.getMethod(declaredMethod);
            }
        }
        throw new RuntimeException("can not found method "+ method +" in class " + bean.getClass().getCanonicalName() + " with params " + params);
    }

    protected void cacheMethod(String serviceAndMethod, FastMethod method) {
        methodCache.put(serviceAndMethod, method);
    }

    protected FastMethod getMethod(String serviceAndMethod) {
        return methodCache.get(serviceAndMethod);
    }

    protected Response castToResponse(Object returnObj) {
        return ResponseFactory.createOkResponse(returnObj);
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
}
