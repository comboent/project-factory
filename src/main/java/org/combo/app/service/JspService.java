package org.combo.app.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class JspService {

    public ModelAndView hello(String name) {
        throw new RuntimeException("yoyoyo");
//        ModelAndView mav = new ModelAndView("index");
//        mav.addObject("name", name);
//        return mav;
    }
}
