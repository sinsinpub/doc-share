package com.jfinal.ext.plugin.guice;

import com.google.inject.Binder;
import com.google.inject.Module;

/**
 * 在默认Module执行完之后会被调用的回调，可以做更多配置操作.
 */
public interface ConfigureModuleCallback {
    /**
     * 和 {@link Module#configure(Binder)} 一样.
     */
    void configure(Binder binder);
}