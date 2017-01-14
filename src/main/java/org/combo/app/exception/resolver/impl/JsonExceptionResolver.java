package org.combo.app.exception.resolver.impl;

import org.combo.app.exception.BusinessException;
import org.combo.app.exception.resolver.ExceptionResolver;
import org.combo.app.net.ResponseFactory;
import org.combo.app.util.JsonUtils;
import org.combo.app.util.MsgUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

public class JsonExceptionResolver extends ExceptionResolver {
    private static final Logger logger = LoggerFactory.getLogger(JsonExceptionResolver.class);
    @Override
    public ModelAndView resolve() {
        if(e instanceof BusinessException) {
            logger.info("business error", e);
            BusinessException ex = (BusinessException) e;
            writeMsg(JsonUtils.toJson(ResponseFactory.createErrResponse(ex.getMsg())));;
        } else {
            logger.error("unknown error", e);
            writeMsg(JsonUtils.toJson(ResponseFactory.createErrResponse(MsgUtils.getMsg("err.sys"))));;
        }
        return null;
    }

    @Override
    protected String getResponseContentType() {
        return "application/json;charset=UTF-8";
    }

}
