package org.combo.app.util;

import org.apache.commons.lang3.StringUtils;
import org.combo.app.net.RequestContext;

import java.util.ArrayList;
import java.util.List;

public class PathUtils {

    public static String createAppPath(String rootPath, String subPath, RequestContext reqCtx) {
        List<String> subPaths = new ArrayList<String>();
        subPaths.add(subPath);
        return createAppPath(rootPath, subPaths, reqCtx);
    }

    public static String createAppPath(String rootPath, List<String> subPaths, RequestContext reqCtx) {
        String root = reqCtx.getSession().getServletContext().getRealPath("/") + "/" + rootPath;
        for (String subPath : subPaths) {
            root += "/" + subPath;
        }
        return root;
    }

    public static String toSaveablePath(String src, RequestContext reqCtx) {
        String saveablePath = src.substring(reqCtx.getSession().getServletContext().getRealPath("/").length() - 1);
        saveablePath = StringUtils.replace(saveablePath, "\\", "/");
        return saveablePath;
    }
}
