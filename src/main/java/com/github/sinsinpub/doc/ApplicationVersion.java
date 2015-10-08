package com.github.sinsinpub.doc;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.io.Closeables;
import com.google.common.primitives.Longs;

/**
 * <p>
 * 应用程序源码、构建、版本、实例配置等元数据信息容器。
 * </p>
 * <p>
 * 信息来自META-INF/application.properties，部分收集自maven、jenkins等。<br>
 * 所以最少需要经过maven构建信息才会更新。要经过jenkins才有会CI相关信息。
 * </p>
 * 
 * @author sin_sin
 * @version $Date: 2014-04-29 $
 */
public class ApplicationVersion implements Serializable, Cloneable {

    /** serialVersionUID */
    private static final long serialVersionUID = -2039327079759677037L;
    /** 应用程序包起始位置 */
    public static final String PACKAGE_ROOT = ApplicationVersion.class.getPackage().getName();
    /** 应用属性文件默认位置 */
    public static final String APP_PROPS_FILE = "/META-INF/application.properties";
    /** 默认实例 */
    private static final ApplicationVersion INSTANCE = new ApplicationVersion();

    /** 本模块的组标识 */
    private String projectGroupId;
    /** 本模块的标识 */
    private String projectArtifactId;
    /** 本模块的版本号 */
    private String projectVersion;
    /** 应用程序名称 */
    private String applicationName;
    /** 应用程序版本 */
    private String applicationVersion;
    /** 构建应用时使用的配置 */
    private String buildProfile;
    /** 构建应用时版本控制管理器的版本号标识 */
    private String scmVersion;
    /** 构建应用时的持续整合管理器的版本号标识 */
    private String ciVersion;

    /**
     * @return 返回默认实例
     */
    public static ApplicationVersion getInstance() {
        return INSTANCE;
    }

    /**
     * 创建新实例并立即加载属性
     */
    public ApplicationVersion() {
        loadProperties(null);
    }

    /**
     * 从属性文件加载属性值
     * 
     * @param propsFile 指定属性文件，为null时从默认位置加载
     * @see #APP_PROPS_FILE
     */
    protected void loadProperties(String propsFile) {
        InputStream is = getClass().getResourceAsStream(
                Strings.isNullOrEmpty(propsFile) ? APP_PROPS_FILE : propsFile);
        Properties app = new Properties();
        try {
            app.load(is);
        } catch (IOException e) {
            // 加载文件失败
            return;
        } finally {
            Closeables.closeQuietly(is);
        }
        parseProperties(app);
    }

    protected void parseProperties(Properties app) {
        projectGroupId = app.getProperty("project.groupId");
        projectArtifactId = app.getProperty("project.artifactId");
        projectVersion = app.getProperty("project.version");
        applicationName = app.getProperty("application.name");
        applicationVersion = app.getProperty("application.version");
        buildProfile = app.getProperty("build.profile");
        String scmRevision = app.getProperty("scm.revision");
        scmVersion = scmRevision + "," + app.getProperty("scm.branch");
        // 将时间戳变成可读时间
        String timestamp = app.getProperty("scm.timestamp");
        Long date = Longs.tryParse(timestamp);
        if (date != null) {
            scmVersion += ","
                    + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date(date));
        }
        ciVersion = app.getProperty("ci.buildTag") + "," + app.getProperty("ci.buildId");
        // 将构建号和版本控制号加入应用版本标识
        String buildNumber = app.getProperty("ci.buildNumber");
        if (!Strings.isNullOrEmpty(buildNumber) && !"${BUILD_NUMBER}".equals(buildNumber)) {
            applicationVersion += ".b" + buildNumber;
        }
        if (!Strings.isNullOrEmpty(scmRevision) && !"${buildNumber}".equals(scmRevision)) {
            applicationVersion += ".r" + scmRevision;
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("projectGroupId", getProjectGroupId())
                .add("projectArtifactId", getProjectArtifactId())
                .add("projectVersion", getProjectVersion())
                .add("applicationName", getApplicationName())
                .add("applicationVersion", getApplicationVersion())
                .add("buildProfile", getBuildProfile())
                .add("scmVersion", getScmVersion())
                .add("ciVersion", getCiVersion())
                .toString();
    }

    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public String getProjectGroupId() {
        return projectGroupId;
    }

    public String getProjectArtifactId() {
        return projectArtifactId;
    }

    public String getProjectVersion() {
        return projectVersion;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

    public String getBuildProfile() {
        return buildProfile;
    }

    public String getScmVersion() {
        return scmVersion;
    }

    public String getCiVersion() {
        return ciVersion;
    }

}
