package org.msc.main.controller;

import com.baidu.ueditor.ActionEnter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@RequestMapping("/editor")
public class UEditorController {

    private static Logger logger = LoggerFactory.getLogger(UEditorController.class);

    @RequestMapping(value="/go")
    public void config(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

        response.setContentType("application/json");
        String rootPath = session.getServletContext().getRealPath("/");
        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            logger.error("ueditor meet an exception", e);
        }

    }
}
