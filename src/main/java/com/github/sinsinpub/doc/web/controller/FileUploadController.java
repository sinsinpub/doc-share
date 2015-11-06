package com.github.sinsinpub.doc.web.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.sinsinpub.doc.web.RoutesDefines;
import com.github.sinsinpub.doc.web.controller.base.JsonAwareController;
import com.github.sinsinpub.doc.web.model.DocFile;
import com.jfinal.aop.Before;
import com.jfinal.ext.interceptor.NoUrlPara;
import com.jfinal.kit.JsonKit;
import com.jfinal.upload.UploadFile;

/**
 * 映射到`/api/upload`处理文件元数据和接收上传数据的控制器.
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
        List<Map<String, ?>> fileItems = new ArrayList<Map<String, ?>>();
        long now = System.currentTimeMillis();
        for (UploadFile uploadFile : files) {
            if (null != uploadFile) {
                logger.info(String.format("Received file: %s", JsonKit.toJson(uploadFile, 1)));
                File fileHandler = uploadFile.getFile();
                String remoteFileName = uploadFile.getOriginalFileName();
                String localFileName = uploadFile.getFileName();
                String mimeType = uploadFile.getContentType();

                DocFile docFile = DocFile.REPO.create(remoteFileName, "hash", mimeType,
                        fileHandler.length(), localFileName, false, now, "admin@yourdomain",
                        getRealRemoteAddr(), now);
                logger.info(String.format("Received file meta: %s", JsonKit.toJson(docFile)));

                Map<String, Object> item = new HashMap<String, Object>();
                item.put("name", localFileName);
                item.put("size", 0l);
                item.put("url", "/download/byname?n=" + localFileName);
                item.put("thumbnail_url", "");
                item.put("delete_url", "");
                item.put("delete_type", "POST");
                fileItems.add(item);
            }
        }
        renderJson("files", fileItems.toArray());

    }

    public void progress() {
        renderJson("status", "ok");
    }

    public void list() {
        renderJson(DocFile.REPO.fetchAll());
    }

}
