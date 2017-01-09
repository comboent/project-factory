package org.msc.main.controller;

import net.sf.cglib.reflect.FastMethod;
import org.apache.commons.lang3.StringUtils;
import org.msc.main.controller.util.ControllerSupport;
import org.msc.main.util.Json;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Controller
public class MainController extends ControllerSupport {

    @RequestMapping("/app" )
    @ResponseBody
    public Object dispatch(HttpServletRequest req, HttpServletResponse resp, HttpSession session) {
        String api = req.getParameter("api");//e.g service.method
        String args = req.getParameter("args");//json数组形式的String e.g ["a", 1, true, 1.5]
        if(StringUtils.isEmpty(api)) {
            throw new RuntimeException("api can not be empty");
        }
        List<Object> params = Json.fromJsonToList(args, Object.class);

        FastMethod method0 = getMethod(api);
        String[] serviceAndMethodArr = api.split("\\.");
        String service = serviceAndMethodArr[0];
        String method = serviceAndMethodArr[1];
        Object serviceBean = applicationContext.getBean(service);
        if(method0 == null) {
            method0 = findMethod(serviceBean, method, params);
            cacheMethod(api, method0);
        }

        try {
            Object returnObj = method0.invoke(serviceBean, params.toArray());
            return returnObj;
        } catch (InvocationTargetException e) {
            return handleException(e.getTargetException(), req, resp, session);
        }
    }

}
