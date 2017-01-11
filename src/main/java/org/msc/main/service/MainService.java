package org.msc.main.service;

import org.msc.main.conf.AppConfig;
import org.msc.main.exception.Code;
import org.msc.main.exception.ExceptionFactory;
import org.msc.main.net.RequestContext;
import org.msc.main.net.Response;
import org.msc.main.net.ResponseFactory;
import org.msc.main.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MainService {

    @Autowired
    private AppConfig appConfig;

    public Response hello(String name, int x, RequestContext ctx) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("hello", name);
        result.put("x", x);
        List<MultipartFile> multipartFiles = ctx.getMultipartFiles();
        for (MultipartFile file : multipartFiles) {
            String fileName = file.getOriginalFilename();
            String s = IOUtils.transferToServer(file, appConfig.getUploadRootPath(), name, fileName, ctx);
            result.put("path", s);
        }
        return ResponseFactory.createOkResponse(result);
    }

    public void error() {
        throw ExceptionFactory.create(Code.USER_NOT_FOUND);
    }
}
