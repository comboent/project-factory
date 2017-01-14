package org.combo.app.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

@Controller
public class KaptchaController {

    @Autowired
    private Producer producer;

    //使用自定义Content-Type类型做Hack 从而使用BufferedImageHttpMessageConverter
    @RequestMapping(value = "/kaptcha")
    public void kaptcha(HttpServletResponse resp, HttpSession session) {
        // Set to expire far in the past.
        resp.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        resp.setHeader("Pragma", "no-cache");

        // create the text for the image
        String capText = producer.createText();
        session.setAttribute(Constants.KAPTCHA_SESSION_CONFIG_KEY, capText);
        session.setAttribute(Constants.KAPTCHA_SESSION_CONFIG_DATE, new Date());

        BufferedImage image = producer.createImage(capText);
        resp.setContentType("image/png");
        ServletOutputStream out = null;
        try {
            out = resp.getOutputStream();
            ImageIO.write(image, "png", out);
            out.flush();
        } catch (IOException e) {
        } finally {
            IOUtils.closeQuietly(out);
        }

    }
}
