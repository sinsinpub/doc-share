package com.jfinal.plugin.activerecord.ext;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import jodd.io.findfile.ClassScanner;
import jodd.util.ClassLoaderUtil;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;

/**
 * 扫描带{@link TableEntity}的注解的{@link Model}类的工具.
 * 
 * @author sin_sin
 * @version $Date: Oct 30, 2015 $
 */
public class AnnotatedModelClassesScanner extends ClassScanner {

    protected static final String CLASSPATH_ROOT = ".";
    protected final byte[] tableEntityAnnotationBytes;
    protected String rootPackagePath;

    protected final Set<Class<? extends Model<?>>> modelClasses = new HashSet<Class<? extends Model<?>>>();

    public AnnotatedModelClassesScanner() {
        tableEntityAnnotationBytes = getTypeSignatureBytes(TableEntity.class);
    }

    public AnnotatedModelClassesScanner scanClasspath() {
        this.scanDefaultClasspath();
        return this;
    }

    public AnnotatedModelClassesScanner scanPackage(String packageName) {
        if (StrKit.isBlank(packageName)) {
            packageName = CLASSPATH_ROOT;
        }
        this.rootPackagePath = packageName;
        this.scan(ClassLoaderUtil.getResourceUrl(packageName.replace(".", "/")));
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onEntry(EntryData entryData) throws Exception {
        String entryName = entryData.getName();
        if (StrKit.notBlank(this.rootPackagePath) && !CLASSPATH_ROOT.equals(this.rootPackagePath)) {
            entryName = String.format("%s.%s", this.rootPackagePath, entryName);
        }
        InputStream inputStream = entryData.openInputStream();
        if (isTypeSignatureInUse(inputStream, tableEntityAnnotationBytes) == false) {
            return;
        }
        Class<?> beanClass = loadClass(entryName);
        if (beanClass == null) {
            return;
        }
        if (!Model.class.isAssignableFrom(beanClass)) {
            return;
        }
        TableEntity tableModel = beanClass.getAnnotation(TableEntity.class);
        if (tableModel == null) {
            return;
        }
        modelClasses.add((Class<? extends Model<?>>) beanClass);
    }

    public Set<Class<? extends Model<?>>> getModelClasses() {
        return Collections.unmodifiableSet(modelClasses);
    }

}
