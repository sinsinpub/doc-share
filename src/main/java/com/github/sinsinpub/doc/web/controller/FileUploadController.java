package com.github.sinsinpub.doc.web.controller;

import java.util.ArrayList;
import java.util.List;

import com.github.sinsinpub.doc.web.RoutesDefines;
import com.github.sinsinpub.doc.web.controller.base.JsonAwareController;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.NoUrlPara;
import com.jfinal.kit.JsonKit;
import com.jfinal.upload.UploadFile;

/**
 * 映射到`/api/upload`处理文件数据上传提交的控制器.
 * 
 * @see RoutesDefines
 * @author sin_sin
 * @version $Date: Oct 27, 2015 $
 */
public class FileUploadController extends JsonAwareController {

    @Before(NoUrlPara.class)
    public void index() {
        renderJson("status", "ok");
    }

    public void any() {
        List<UploadFile> files = getFiles();
        List<String> fileNames = new ArrayList<String>();
        for (UploadFile uploadFile : files) {
            if (null != uploadFile) {
                String fileName = uploadFile.getFileName();
                logger.info(String.format("Received file: %s", JsonKit.toJson(uploadFile, 1)));
                fileNames.add(fileName);
            }
        }
        renderJson(fileNames.toArray());
    }

    public void progress() {
        renderJson("status", "ok");
    }

}
