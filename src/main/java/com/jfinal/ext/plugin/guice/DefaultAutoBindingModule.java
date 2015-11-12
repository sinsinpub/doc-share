package com.jfinal.ext.plugin.guice;

import java.util.Map;
import java.util.Map.Entry;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provider;

/**
 * 实现自动简单绑定的默认Guice Module.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DefaultAutoBindingModule extends AbstractModule {

    protected final Map<Class, ?> bindMap;
    protected final ConfigureModuleCallback moduleCallback;

    DefaultAutoBindingModule(Map bindMap, ConfigureModuleCallback callback) {
        this.bindMap = bindMap;
        this.moduleCallback = callback;
    }

    @Override
    public void configure() {
        if (bindMap != null && !bindMap.isEmpty()) {
            for (Entry<Class, ?> entry : bindMap.entrySet()) {
                Class bindSrc = entry.getKey();
                Object bindTarget = entry.getValue();
                if (bindSrc != null) {
                    if (bindTarget instanceof Class) {
                        bind(bindSrc).to((Class) bindTarget);
                    } else if (bindTarget instanceof Key) {
                        // 这里Key被我们特殊用法了，里面的类型是目标实现类，不是绑定源
                        Key key = (Key) bindTarget;
                        bind(bindSrc).annotatedWith(key.getAnnotation()).to(
                                key.getTypeLiteral().getRawType());
                    } else if (bindTarget instanceof Provider) {
                        bind(bindSrc).toProvider((Provider) bindTarget);
                    } else {
                        bind(bindSrc);
                    }
                }
            }
        }
        if (moduleCallback != null) {
            moduleCallback.configure(binder());
        }
    }

}
