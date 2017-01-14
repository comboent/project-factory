package org.combo.app.exception.resolver.impl;

import org.combo.app.exception.resolver.ExceptionResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

public class JspExceptionResolver extends ExceptionResolver {
    private static final Logger logger = LoggerFactory.getLogger(JspExceptionResolver.class);

    @Override
    public ModelAndView resolve() {
        return new ModelAndView("err").addObject("cause", e.getMessage());
    }

    @Override
    protected String getResponseContentType() {
        return "text/html;charset=UTF-8";
    }
}
