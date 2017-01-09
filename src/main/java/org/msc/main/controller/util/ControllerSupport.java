package org.msc.main.controller.util;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControllerSupport implements ApplicationContextAware {
    protected ApplicationContext applicationContext;

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
     * @return
     */
    protected FastMethod findMethod(Object bean, String method, List<Object> params) {
        Class<?> clazz = bean.getClass();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if(declaredMethod.getName().equals(method) && declaredMethod.getParameterTypes().length == params.size()) {
                FastClass fastClass = FastClass.create(clazz);
                return fastClass.getMethod(declaredMethod);
            }
        }
        throw new RuntimeException("can not find method " + method + " in class " + clazz.getCanonicalName() + " with param " + params);
    }

    protected void cacheMethod(String serviceAndMethod, FastMethod method) {
        methodCache.put(serviceAndMethod, method);
    }

    protected FastMethod getMethod(String serviceAndMethod) {
        return methodCache.get(serviceAndMethod);
    }

    protected ErrObj handleException(Throwable targetException, HttpServletRequest req, HttpServletResponse resp, HttpSession session) {
        ErrObj obj = new ErrObj();
        obj.setCode(0);
        obj.setMsg(targetException.getMessage());
        return obj;
    }

    class ErrObj {
        private int code;
        private String msg;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
