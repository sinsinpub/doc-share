package com.github.sinsinpub.doc.web.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.ext.TableEntity;

/**
 * 被匿名或注册用户上传下载的文件(档)对象实体.
 * 
 * @author sin_sin
 * @version $Date: Oct 8, 2015 $
 */
@TableEntity(DocFile.TABLE)
public class DocFile extends Model<DocFile> {

    /** serialVersionUID */
    private static final long serialVersionUID = 1L;

    public static final String TABLE = "DocFile";
    public static final String ID = "id";
    public static final String HASH = "hash";
    public static final String NAME = "name";
    public static final String MIME = "mime";
    public static final String SIZE = "size";
    public static final String LOCATION = "res_loc";
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

    public DocFile create(String name, String hash, String mime, Long size, String fileloc,
            Boolean isTxt, Long discontTime, String user, String from, Long uploadTime) {
        // check if file (hash) already exists
        // if (findByHash(hash) != null) {
        // }
        DocFile file = new DocFile();
        file.set(ID, UUID.randomUUID().toString());
        file.set(NAME, name);
        file.set(HASH, hash);
        file.set(MIME, mime);
        file.set(SIZE, size);
        file.set(LOCATION, fileloc);
        file.set(IS_PLAINTEXT, isTxt);
        if (discontTime != null) {
            file.set(DISCONTINUE_AFTER, new Timestamp(discontTime));
        }
        file.set(BELONG_USER, user);
        file.set(UPLOAD_FROM, from);
        if (uploadTime != null) {
            file.set(UPLOAD_AT, new Timestamp(uploadTime));
        }
        file.save();
        return file;
    }

    public List<DocFile> findByHash(String hash) {
        return find(String.format("select * from %s where %s = ?", TABLE, HASH), hash);
    }

    public List<DocFile> fetchAll() {
        return REPO.find(String.format("select * from %s order by %s desc", TABLE, UPLOAD_AT));
    }

}
