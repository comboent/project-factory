package org.msc.main.net;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class RequestContext {
    private HttpServletRequest request;

    private HttpServletResponse response;

    private HttpSession session;

    private List<MultipartFile> multipartFiles;

    public RequestContext(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        this.request = request;
        this.response = response;
        this.session = session;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public HttpSession getSession() {
        return session;
    }

    public List<MultipartFile> getMultipartFiles() {
        return multipartFiles;
    }

    public void addMultipartFile(MultipartFile file) {
        if(multipartFiles == null) {
            multipartFiles = new ArrayList<MultipartFile>();
        }
        this.multipartFiles.add(file);
    }
}
