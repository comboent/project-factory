package org.combo.app.exception.resolver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class ExceptionResolver {
    private static Logger logger = LoggerFactory.getLogger(ExceptionResolver.class);

    protected HttpServletRequest req;
    protected HttpServletResponse resp;
    protected Exception e;

    public void init(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        this.req = req;
        this.resp = resp;
        this.e = e;
    }

    public abstract ModelAndView resolve();

    protected abstract String getResponseContentType();

    protected void writeMsg(String msg) {
        try {
            resp.setDateHeader("Expires", 0);
            resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");//标准HTTP/1.1禁止缓存头
            resp.addHeader("Cache-Control", "post-check=0, pre-check=0");//适配IE HTTP/1.1
            resp.setHeader("Pragma", "no-cache");//适配HTTP/1.0
            resp.setContentType(getResponseContentType());
            PrintWriter writer = resp.getWriter();
            writer.write(msg);
            writer.flush();
        } catch (IOException e) {
            logger.error("can not get writer from response");
        }
    }
}
