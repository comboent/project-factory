package org.combo.app.controller;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.util.Date;
import java.util.Properties;

@Controller
public class KaptchaController {

    @Autowired
    private Properties kaptcha;

    private Producer producer;

    private Config config;

    @PostConstruct
    private void init() {
        ImageIO.setUseCache(false);
        Config config = new Config(kaptcha);
        this.config = config;
        producer = config.getProducerImpl();
    }

    //使用自定义Content-Type类型做Hack 从而使用BufferedImageHttpMessageConverter
    @RequestMapping(value = "/kaptcha", consumes = "app/kaptcha")
    @ResponseBody
    public BufferedImage kaptcha(HttpServletResponse resp, HttpSession session) {
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
        session.setAttribute(config.getSessionKey(), capText);
        session.setAttribute(config.getSessionDate(), new Date());

        return producer.createImage(capText);
    }
}
