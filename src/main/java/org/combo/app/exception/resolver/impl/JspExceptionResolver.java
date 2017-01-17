package org.combo.app.exception.resolver.impl;

import org.combo.app.exception.BusinessException;
import org.combo.app.exception.resolver.ExceptionResolver;
import org.combo.app.util.MsgUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

public class JspExceptionResolver extends ExceptionResolver {
    private static final Logger logger = LoggerFactory.getLogger(JspExceptionResolver.class);

    @Override
    public ModelAndView resolve() {
        if(e instanceof BusinessException) {
            BusinessException ex = (BusinessException) e;
            return new ModelAndView("err").addObject("cause", ex.getMsg());
        } else {
            return new ModelAndView("err").addObject("cause", MsgUtils.getMsg("err.sys"));
        }
    }

    @Override
    protected String getResponseContentType() {
        return "text/html;charset=UTF-8";
    }
}
