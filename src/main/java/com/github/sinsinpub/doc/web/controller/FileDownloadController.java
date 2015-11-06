package com.github.sinsinpub.doc.web.controller;

import com.github.sinsinpub.doc.web.RoutesDefines;
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
        String filename = getPara("n");
        renderJson("file", filename);
    }

}
