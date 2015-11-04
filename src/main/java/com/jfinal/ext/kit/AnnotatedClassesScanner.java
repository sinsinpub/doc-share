package com.jfinal.ext.kit;

import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jodd.io.findfile.ClassScanner;
import jodd.util.ClassLoaderUtil;

import com.github.sinsinpub.doc.hint.NotThreadSafe;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;

/**
 * 扫描带有指定类注解的指定子类的工具.
 * 
 * @author sin_sin
 * @version $Date: Oct 30, 2015 $
 */
@NotThreadSafe
public class AnnotatedClassesScanner extends ClassScanner {

    /** 默认ClassPath根目录表达式(因为包名风格'.'转成文件系统风格是'/') */
    protected static final String CLASSPATH_ROOT = ".";
    /** 应用日志记录器 */
    protected final Logger logger = Logger.getLogger(getClass());
    /** 只搜索派生自指定类或接口的类型 */
    protected final Class<?> targetClass;
    /** 只搜索被指定类注解的类型 */
    protected final Class<? extends Annotation> targetAnnotation;
    /** 快速识别注解用的特征字节码缓存 */
    protected final byte[] targetAnnotationBytes;
    /** 搜索开始路径 */
    protected String rootPackagePath;

    /** 扫描到的类结果集 */
    protected final Set<Class<?>> matchedClasses = new HashSet<Class<?>>();

    /**
     * 新建不带过滤条件的扫描器实例.
     * <p>
     * 直接会匹配出所有类，不建议使用。最少应指定一种过滤条件。
     * 
     * @see #AnnotatedClassesScanner(Class, Class)
     */
    public AnnotatedClassesScanner() {
        targetClass = null;
        targetAnnotation = null;
        targetAnnotationBytes = null;
        setIgnoreException(true);
    }

    /**
     * 用指定过滤条件新建扫描器实例.
     * 
     * @param classType 应派生自指定类或接口的类型，null表示匹配任意类型
     * @param annotationType 应被指定类注解的类型，null表示不需要匹配类注解
     */
    public AnnotatedClassesScanner(Class<?> classType, Class<? extends Annotation> annotationType) {
        targetClass = classType;
        targetAnnotation = annotationType;
        targetAnnotationBytes = targetAnnotation == null ? null
                : getTypeSignatureBytes(annotationType);
        setIgnoreException(true);
    }

    /**
     * 自动扫描整个ClassPath.
     * 
     * @return 本工具自身
     */
    public AnnotatedClassesScanner scanClasspath() {
        logger.debug(String.format("Scanning classes for >%s (@%s) at default CLASSPATH",
                targetClass, targetAnnotation));
        super.scanDefaultClasspath();
        return this;
    }

    /**
     * 扫描指定包开始的子目录.
     * 
     * @param packageName 包名
     * @return 本工具自身
     */
    public AnnotatedClassesScanner scanPackage(String packageName) {
        if (StrKit.isBlank(packageName)) {
            packageName = CLASSPATH_ROOT;
        }
        rootPackagePath = packageName;
        logger.debug(String.format("Scanning classes for >%s (@%s) at: %s", targetClass,
                targetAnnotation, rootPackagePath));
        super.scan(ClassLoaderUtil.getResourceUrl(packageName.replace(".", "/")));
        return this;
    }

    @Override
    protected void onEntry(EntryData entryData) throws Exception {
        String entryName = entryData.getName();
        // 当类名的完整包前缀被父Scanner去除时，重新加回来
        if (StrKit.notBlank(rootPackagePath) && !CLASSPATH_ROOT.equals(rootPackagePath)) {
            entryName = String.format("%s.%s", rootPackagePath, entryName);
        }
        // 先不加载类，从类文件字节码特征判断是否存在指定类注解性能较好
        if (targetAnnotationBytes != null) {
            InputStream inputStream = entryData.openInputStream();
            if (!isTypeSignatureInUse(inputStream, targetAnnotationBytes)) {
                return;
            }
        }
        // 然后尝试加载类
        Class<?> beanClass = loadClass(entryName);
        if (beanClass == null) {
            logger.warn(String.format("Cannot load class: %s", entryName));
            return;
        }
        // 过滤看是否真的能取得指定类注解
        if (targetAnnotation != null) {
            Annotation anno = beanClass.getAnnotation(targetAnnotation);
            if (anno == null) {
                return;
            }
        }
        // 过滤看是否指定类的派生类
        if (targetClass != null && !targetClass.isAssignableFrom(beanClass)) {
            return;
        }
        logger.debug(String.format("Matched class: %s", beanClass.getName()));
        matchedClasses.add(beanClass);
    }

    /**
     * 获取当前扫描结果.
     * <p>
     * 必须在某个scan方法执行之后才可能有结果。<br>
     * 注意多次执行scan时并不会自动清除上次扫描结果。要清除需要手工执行{@link #clearResults()}。
     * 
     * @return 满足类型和注解条件的类型集。如果没有结果会返回空集，不会返回null。
     */
    public Set<Class<?>> getResults() {
        return Collections.unmodifiableSet(matchedClasses);
    }

    /**
     * 清除当前扫描结果.
     */
    public void clearResults() {
        matchedClasses.clear();
    }

}
