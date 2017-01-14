package org.combo.app.controller;

import net.sf.cglib.reflect.FastMethod;
import org.apache.commons.lang3.StringUtils;
import org.combo.app.conf.AppConfig;
import org.combo.app.controller.util.ControllerSupport;
import org.combo.app.exception.Code;
import org.combo.app.exception.ExceptionFactory;
import org.combo.app.net.RequestContext;
import org.combo.app.net.Response;
import org.combo.app.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

@Controller
public class JsonController extends ControllerSupport {

    private static final Logger logger = LoggerFactory.getLogger(JsonController.class);

    private CommonsMultipartResolver multipartResolver;

    @PostConstruct
    private void init() {
        //完美Hack百度Ueditor与CommonsMultipartResolver在文件上传时冲突问题
        multipartResolver = new CommonsMultipartResolver(servletContext);
        multipartResolver.setDefaultEncoding("utf-8");
        multipartResolver.setMaxInMemorySize(131072);
        multipartResolver.setMaxUploadSize(-1);
    }

    @Autowired
    private AppConfig appConfig;

    @RequestMapping("/app")
    @ResponseBody
    public Response dispatch(HttpServletRequest req, HttpServletResponse resp, HttpSession session) throws Throwable {
        boolean isMultipart = multipartResolver.isMultipart(req);
        if (isMultipart) {
            req = multipartResolver.resolveMultipart(req);
        }
        String api = req.getParameter("api");//e.g service.method
        String args = req.getParameter("args");//json数组形式的String e.g ["a", 1, true, 1.5]
        if (StringUtils.isEmpty(api)) {
            throw ExceptionFactory.create(Code.API_ACCESS_ILLEGAL, api);
        }
        List<Object> params = JsonUtils.fromJsonToList(args, Object.class);
        RequestContext ctx = new RequestContext(req, resp, session);
        params.add(ctx);
        if (isMultipart) {
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req;
            Iterator<String> iter = multiRequest.getFileNames();
            while (iter.hasNext()) {
                MultipartFile file = multiRequest.getFile(iter.next());
                //这里服务器不拒绝已经传入的文件 前端去验证上传文件的大小 否则用户体验不好
                if (file.getSize() > appConfig.getUploadLimit()) {
                    throw ExceptionFactory.create(Code.UPLOAD_EXCEED_LIMIT);
                }
                if (file != null) {
                    ctx.addMultipartFile(file);
                }
            }
        }

        String[] serviceAndMethodArr = api.split("\\.");
        if (serviceAndMethodArr.length != 2) {
            throw ExceptionFactory.create(Code.API_ACCESS_ILLEGAL, api);
        }
        String service = serviceAndMethodArr[0];
        String method = serviceAndMethodArr[1];
        Object serviceBean = applicationContext.getBean(service);
        FastMethod fastMethod = getMethod(api);
        if (fastMethod == null) {
            fastMethod = findMethod(serviceBean, method, params, api);
            cacheMethod(api, fastMethod);
        }

        try {
            Object returnObj = fastMethod.invoke(serviceBean, params.toArray());
            if (returnObj instanceof Response) {
                return (Response) returnObj;
            } else {
                logger.error("unsupported response object found! location:" + api + " it is really unrecommended! fix it using Response.class");
                return castToResponse(returnObj);
            }
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

}
