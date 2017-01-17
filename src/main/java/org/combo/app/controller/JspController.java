package org.combo.app.controller;

import org.combo.app.controller.util.ControllerSupport;
import org.combo.app.service.JspService;
import org.combo.app.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 使用该controller的时候需要将ExceptionHandler加入Spring容器 用来处理全局异常
 * 如果涉及文件上传 还需要将ac-mvc.xml中CommonsMultipartResolver解开
 */
@Controller
@RequestMapping("/jsp")
public class JspController extends ControllerSupport{

    private static final Logger logger = LoggerFactory.getLogger(JspController.class);

    @Autowired
    private JspService jspService;

    @Autowired
    private UserService userService;

    @RequestMapping("/hello")
    public ModelAndView hello(@RequestParam String name) {
        return jspService.hello(name);
    }

    @RequestMapping("/showReg")
    public ModelAndView showReg() {
        return new ModelAndView("reg");
    }

    @RequestMapping("/reg")
    public ModelAndView reg(@RequestParam String username, @RequestParam String pwd) {
        return userService.regUser(username, pwd);
    }

    @RequestMapping("/login")
    public ModelAndView login(@RequestParam String username, @RequestParam String pwd) {
        return userService.login(username, pwd);
    }
    @RequestMapping("/subject")
    public ModelAndView subject() {
        return userService.subjectInfo();
    }

}
