package com.jfinal.ext.plugin.activerecord;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解式为Model声明表映射时使用.
 * <p>
 * 配置应用插件时只要用{@link AutoMappingTablesActiveRecordPlugin}替换<code>ActiveRecordPlugin</code>即可。
 * 
 * @author sin_sin
 * @version $Date: Oct 30, 2015 $
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface TableEntity {
    /** 表名(默认属性) */
    String value() default "";

    /** 表名 */
    String name() default "";

    /** 主键名 */
    String pk() default "";
}
