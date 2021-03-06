package com.jfinal.ext.plugin.activerecord;

import java.util.Set;

import javax.sql.DataSource;

import com.jfinal.ext.kit.AnnotatedClassesScanner;
import com.jfinal.kit.StrKit;
import com.jfinal.log.Logger;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Config;
import com.jfinal.plugin.activerecord.IDataSourceProvider;
import com.jfinal.plugin.activerecord.Model;

/**
 * 支持扫描带{@link TableEntity}注解的Model类，自动注册映射.
 * 
 * @see ActiveRecordPlugin
 * @author sin_sin
 * @version $Date: Oct 30, 2015 $
 */
public class AutoMappingTablesActiveRecordPlugin extends ActiveRecordPlugin {

    protected Logger logger = Logger.getLogger(getClass());
    protected String modelPackageToScan;

    public AutoMappingTablesActiveRecordPlugin(Config config) {
        super(config);
    }

    public AutoMappingTablesActiveRecordPlugin(DataSource dataSource, int transactionLevel) {
        super(dataSource, transactionLevel);
    }

    public AutoMappingTablesActiveRecordPlugin(DataSource dataSource) {
        super(dataSource);
    }

    public AutoMappingTablesActiveRecordPlugin(IDataSourceProvider dataSourceProvider,
            int transactionLevel) {
        super(dataSourceProvider, transactionLevel);
    }

    public AutoMappingTablesActiveRecordPlugin(IDataSourceProvider dataSourceProvider) {
        super(dataSourceProvider);
    }

    public AutoMappingTablesActiveRecordPlugin(String configName, DataSource dataSource,
            int transactionLevel) {
        super(configName, dataSource, transactionLevel);
    }

    public AutoMappingTablesActiveRecordPlugin(String configName, DataSource dataSource) {
        super(configName, dataSource);
    }

    public AutoMappingTablesActiveRecordPlugin(String configName,
            IDataSourceProvider dataSourceProvider, int transactionLevel) {
        super(configName, dataSourceProvider, transactionLevel);
    }

    public AutoMappingTablesActiveRecordPlugin(String configName,
            IDataSourceProvider dataSourceProvider) {
        super(configName, dataSourceProvider);
    }

    @Override
    public boolean start() {
        autoAddMappings();
        return super.start();
    }

    @SuppressWarnings("unchecked")
    protected void autoAddMappings() {
        // 在指定包下扫描带@TableEntity注解的所有Model类
        AnnotatedClassesScanner scanner = new AnnotatedClassesScanner(Model.class, TableEntity.class);
        Set<Class<?>> modelClasses = StrKit.isBlank(getModelPackageToScan()) ? scanner.scanClasspath()
                .getResults()
                : scanner.scanPackage(getModelPackageToScan()).getResults();
        logger.info(String.format("Found Model annotated by @TableEntity classes: %d",
                modelClasses.size()));
        for (Class<?> c : modelClasses) {
            Class<Model<?>> mc = (Class<Model<?>>) c;
            String cn = mc.getSimpleName();
            TableEntity te = (TableEntity) mc.getAnnotation(TableEntity.class);
            String tn = StrKit.isBlank(te.name()) ? te.value() : te.name();
            tn = StrKit.isBlank(tn) ? cn : tn;
            String pk = te.pk();
            if (StrKit.notBlank(pk)) {
                logger.debug(String.format(
                        "Auto mapping table '%s' to class '%s' with primary key '%s'", tn, cn, pk));
                addMapping(tn, pk, mc);
            } else {
                logger.debug(String.format("Auto mapping table '%s' to class '%s'", tn, cn));
                addMapping(tn, mc);
            }
        }
    }

    @Override
    public boolean stop() {
        return super.stop();
    }

    public String getModelPackageToScan() {
        return modelPackageToScan;
    }

    public void setModelPackageToScan(String modelPackageToScan) {
        this.modelPackageToScan = modelPackageToScan;
    }

}
