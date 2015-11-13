package com.github.sinsinpub.doc.web.controller;

import java.io.File;

import com.github.sinsinpub.doc.web.RoutesDefines;
import com.github.sinsinpub.doc.web.WebAppConfig;
import com.github.sinsinpub.doc.web.controller.base.JsonAwareController;

/**
 * 映射到`/download`处理文件下载的控制器.
 * 
 * @see RoutesDefines
 * @author sin_sin
 * @version $Date: Oct 27, 2015 $
 */
public class FileDownloadController extends JsonAwareController {

    public void index() {
        renderJson("status", "ok");
    }

    public void byname() {
        String fileName = getPara("n");
        renderJson("found", getRealFile(fileName).exists());
    }

    public void unsafe() {
        String fileName = getPara("n");
        renderFile(getRealFile(fileName));
    }

    private File getRealFile(String fileName) {
        File path = new File(WebAppConfig.getConstants().getUploadedFileSaveDirectory());
        return new File(path, new File(fileName).getName());
    }

}
