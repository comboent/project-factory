package org.msc.main.service;

import org.msc.main.exception.Code;
import org.msc.main.exception.ExceptionFactory;
import org.msc.main.net.RequestContext;
import org.msc.main.net.Response;
import org.msc.main.net.ResponseFactory;
import org.msc.main.util.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MainService {
    public Response hello(String name, int x, RequestContext ctx) {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("hello", name);
        result.put("x", x);
        List<MultipartFile> multipartFiles = ctx.getMultipartFiles();
        for (MultipartFile file : multipartFiles) {
            String fileName = file.getOriginalFilename();
            IOUtils.transferTo(file, "C:\\homeworkspace\\project-factory\\upload\\" + name, fileName);
        }
        return ResponseFactory.createOkResponse(result);
    }

    public void error() {
        throw ExceptionFactory.create(Code.USER_NOT_FOUND);
    }
}
