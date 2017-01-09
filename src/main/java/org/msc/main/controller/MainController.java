package org.msc.main.controller;

import net.sf.cglib.reflect.FastMethod;
import org.apache.commons.lang3.StringUtils;
import org.msc.main.controller.util.ControllerSupport;
import org.msc.main.exception.Code;
import org.msc.main.exception.ExceptionFactory;
import org.msc.main.net.Response;
import org.msc.main.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @RequestMapping("/app" )
    @ResponseBody
    public Response dispatch(HttpServletRequest req, HttpServletResponse resp, HttpSession session) {
        try {
            String api = req.getParameter("api");//e.g service.method
            String args = req.getParameter("args");//json数组形式的String e.g ["a", 1, true, 1.5]
            if(StringUtils.isEmpty(api)) {
                throw ExceptionFactory.create(Code.API_ACCESS_ILLEGAL, "err.unknownAPI", api);
            }
            List<Object> params = JsonUtils.fromJsonToList(args, Object.class);

            String[] serviceAndMethodArr = api.split("\\.");
            if(serviceAndMethodArr.length != 2) {
                throw ExceptionFactory.create(Code.API_ACCESS_ILLEGAL, "err.unknownAPI", api);
            }
            String service = serviceAndMethodArr[0];
            String method = serviceAndMethodArr[1];
            Object serviceBean = applicationContext.getBean(service);
            FastMethod fastMethod = getMethod(api);
            if(fastMethod == null) {
                fastMethod = findMethod(serviceBean, method, params, api);
                cacheMethod(api, fastMethod);
            }

            try {
                Object returnObj = fastMethod.invoke(serviceBean, params.toArray());
                if(returnObj instanceof Response) {
                    return (Response) returnObj;
                } else {
                    logger.error("unsupported response object found! location:" + api + " it is really unrecommended! fix it to Response.class");
                    return castToResponse(returnObj);
                }
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            }
        } catch (Throwable e) {
            return handleException(e);
        }
    }
}
