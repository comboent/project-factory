package org.msc.main.util;

import org.msc.main.net.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IOUtils {
    private static final Logger logger = LoggerFactory.getLogger(IOUtils.class);

    public static String transferTo(MultipartFile src, File destPath, String destFileName) {
        if(!destPath.exists()) {
            destPath.mkdirs();
        }
        try {
            File dest = new File(destPath, destFileName);
            src.transferTo(dest);
            return dest.getAbsolutePath();
        } catch (IOException e) {
            logger.error("transfer file failed!", e);
        }
        return "";
    }

    /**
     * 传送文件到服务器指定目录
     * @param src
     * @param rootPath
     * @param subPath
     * @param destFileName
     * @param ctx
     * @return 返回相对于服务器的路径
     */
    public static String transferToServer(MultipartFile src, String rootPath, String subPath, String destFileName, RequestContext ctx) {
        List<String> subPaths = new ArrayList<String>();
        subPaths.add(subPath);
        return transferToServer(src, rootPath, subPaths, destFileName, ctx);
    }

    public static String transferToServer(MultipartFile src, String rootPath, List<String> subPaths, String destFileName, RequestContext ctx) {
        String appPath = PathUtils.createAppPath(rootPath, subPaths, ctx);
        File destPath = new File(appPath);
        String uploadPath = transferTo(src, destPath, destFileName);
        return PathUtils.toSaveablePath(uploadPath, ctx);
    }
}
