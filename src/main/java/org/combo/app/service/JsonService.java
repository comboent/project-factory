package org.combo.app.service;

import org.combo.app.conf.AppConfig;
import org.combo.app.exception.Code;
import org.combo.app.exception.ExceptionFactory;
import org.combo.app.net.RequestContext;
import org.combo.app.net.Response;
import org.combo.app.net.ResponseFactory;
import org.combo.app.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JsonService {

    @Autowired
    private AppConfig appConfig;

    public Response hello(String name, int x, RequestContext ctx) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("hello", name);
        result.put("x", x);
        List<MultipartFile> multipartFiles = ctx.getMultipartFiles();
        if(multipartFiles != null) {
            for (MultipartFile file : multipartFiles) {
                String fileName = file.getOriginalFilename();
                String s = IOUtils.transferToServer(file, appConfig.getUploadRootPath(), name, fileName, ctx);
                result.put("path", s);
            }
        }
        return ResponseFactory.createOkResponse(result);
    }

    public void error() {
        throw ExceptionFactory.create(Code.USER_NOT_FOUND);
    }
}
