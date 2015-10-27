package com.jfinal.upload;

import java.io.File;
import java.io.IOException;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.oreilly.servlet.multipart.FileRenamePolicy;

/**
 * OreillyCos.
 */
public class OreillyCos {

    public static void init(String saveDirectory, int maxPostSize, String encoding) {
        try {
            Class.forName("com.oreilly.servlet.MultipartRequest");
            doInit(saveDirectory, maxPostSize, encoding);
        } catch (ClassNotFoundException e) {

        }
    }

    public static void setFileRenamePolicy(FileRenamePolicy fileRenamePolicy) {
        if (fileRenamePolicy == null)
            throw new IllegalArgumentException("fileRenamePolicy can not be null.");
        MultipartRequest.fileRenamePolicy = fileRenamePolicy;
    }

    private static void doInit(String saveDirectory, int maxPostSize, String encoding) {
        String dir;
        if (StrKit.isBlank(saveDirectory)) {
            dir = PathKit.getWebRootPath() + File.separator + "upload";
        } else if (isRelativePath(saveDirectory) || isAbsolutelyPath(saveDirectory)) {
            if (isRelativePath(saveDirectory)) {
                try {
                    dir = new File(saveDirectory).getCanonicalPath();
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            } else {
                dir = saveDirectory;
            }
        } else {
            dir = PathKit.getWebRootPath() + File.separator + saveDirectory;
        }

        // add "/" postfix
        if (dir.endsWith("/") == false && dir.endsWith("\\") == false) {
            dir = dir + File.separator;
        }

        MultipartRequest.init(dir, maxPostSize, encoding);
    }

    private static boolean isRelativePath(String saveDirectory) {
        return saveDirectory.startsWith(".");
    }

    private static boolean isAbsolutelyPath(String saveDirectory) {
        return saveDirectory.startsWith("/") || saveDirectory.indexOf(":") == 1;
    }

}
