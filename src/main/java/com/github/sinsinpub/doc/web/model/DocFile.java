package com.github.sinsinpub.doc.web.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 被匿名或注册用户上传下载的文件(档)对象实体.
 * 
 * @author sin_sin
 * @version $Date: Oct 8, 2015 $
 */
public class DocFile extends Model<DocFile> {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    public static final String TABLE = "DocFile";
    public static final String ID = "id";
    public static final String HASH = "hash";
    public static final String NAME = "name";
    public static final String MIME = "mime";
    public static final String IS_PLAINTEXT = "is_plaintext";
    public static final String DOWNLOAD_COUNT = "dl_count";
    public static final String DISCONTINUE_AFTER = "discont_after";
    public static final String IS_BLOCKED = "blocked";
    public static final String BELONG_USER = "user";
    public static final String UPLOAD_FROM = "upload_from";
    public static final String UPLOAD_AT = "upload_at";
    public static final String LAST_DOWNLOAD_AT = "last_dl_at";

    public static final DocFile REPO = new DocFile();

    public DocFile() {
    }

}
