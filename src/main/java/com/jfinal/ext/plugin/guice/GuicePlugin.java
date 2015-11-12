package com.jfinal.ext.plugin.guice;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import jodd.util.ReflectUtil;

import com.alibaba.druid.util.StringUtils;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.name.Names;
import com.jfinal.ext.kit.AnnotatedClassesScanner;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;

/**
 * 利用JFinal的Plugin来引导Guice Injector.
 * <p>
 * 当前支持在Plugin开始时，对ClassPath下的类进行扫描。<br>
 * 从扫描得到的类上寻找JSR-250的@{@link Resource}注解。根据注解的属性添批量加Guice的binding。<br>
 * 如果需要进行更多复杂的Module配置，如注册拦截器，可以编写{@link ConfigureModuleCallback}自己完成更多配置。<br>
 * 顺便Guice的一般类到类Inject要求对interface类注入实现类，所以组件应面向接口编写。<br>
 * <p>
 * JFinal作者主张自己new实例，而Google不主张自动扫描来绑定，反正要怎么用，你看着办吧(笑。不完全整合时需要注意的就是哪些实例是JFinal创建管理的，哪些是在Guice
 * Module内由它管理的。 例如Controller、Interceptor实例都是由JFinal管理的，无法直接从Guice注入依赖。
 * 目前的示例实现的是由Guice管理Model层之后的实例注入和它们间的依赖关系图。
 * 
 * @author sin_sin
 * @version $Date: Nov 12, 2015 $
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GuicePlugin implements IPlugin {

    protected final Logger logger = Logger.getLogger(getClass());

    protected static Injector injector;
    private Set<Class> excludeClasses = Sets.newHashSet();
    private Map<Class, Object> bindMap = Maps.newHashMap();
    private ConfigureModuleCallback moduleCallback;

    /**
     * 创建空插件实例.
     */
    public GuicePlugin() {
    }

    /**
     * 手工添加绑定.
     * 
     * @param from 源接口类
     * @param to 目标实现类
     * @return GuicePlugin
     */
    public GuicePlugin bind(Class from, Class to) {
        bindMap.put(from, to);
        return this;
    }

    /**
     * 手工添加绑定.
     * 
     * @param from 源接口类
     * @param name 被@Named注解声明的名称
     * @param to 目标实现类
     * @return GuicePlugin
     */
    public GuicePlugin bind(Class from, String name, Class to) {
        bindMap.put(from, Key.get(to, Names.named(name)));
        return this;
    }

    /**
     * 添加扫描排除类.
     * 
     * @param types 要排除的类
     * @return GuicePlugin
     */
    public GuicePlugin addExcludeClasses(Class... types) {
        if (types != null) {
            for (Class type : types) {
                excludeClasses.add(type);
            }
        }
        return this;
    }

    /**
     * 添加扫描排除类.
     * 
     * @param typeCollection 要排除的类
     * @return GuicePlugin
     */
    public GuicePlugin addExcludeClasses(Collection<Class> typeCollection) {
        if (typeCollection != null) {
            excludeClasses.addAll(typeCollection);
        }
        return this;
    }

    public ConfigureModuleCallback getModuleCallback() {
        return moduleCallback;
    }

    public GuicePlugin setModuleCallback(ConfigureModuleCallback moduleCallback) {
        this.moduleCallback = moduleCallback;
        return this;
    }

    public static <T> T getInstance(Class<T> type) {
        Preconditions.checkState(injector != null, "Guice plugin has not been started.");
        return injector.getInstance(type);
    }

    public static Injector getInjector() {
        return injector;
    }

    @Override
    public boolean start() {
        if (isRunning()) {
            return false;
        }
        scanClassPathForBindingDefinition();
        injector = Guice.createInjector(new DefaultAutoBindingModule());
        return true;
    }

    @Override
    public boolean stop() {
        if (isRunning()) {
            injector = null;
        }
        return true;
    }

    public static boolean isRunning() {
        return injector != null;
    }

    protected void scanClassPathForBindingDefinition() {
        AnnotatedClassesScanner scanner = new AnnotatedClassesScanner(Object.class, Resource.class);
        Set<Class<?>> classes = scanner.scanClasspath().getResults();
        logger.info(String.format("Found Component annotated by @Resource classes: %d",
                classes.size()));
        for (Class type : classes) {
            if (excludeClasses.contains(type)) {
                logger.debug(String.format("Ignored as matched with exclude classes: %s",
                        type.getName()));
                continue;
            }
            // 发现TYPE上的@Resource
            Resource resOnType = (Resource) type.getAnnotation(Resource.class);
            if (resOnType != null) {
                addResourceToBindMap(resOnType, type);
            }
            // 发现FIELD上的@Resource
            for (Field field : ReflectUtil.getAccessibleFields(type)) {
                Resource resOnField = field.getAnnotation(Resource.class);
                if (resOnField != null) {
                    addResourceToBindMap(resOnField, field.getType());
                }
            }
            // 由于@Resource并不能应用在方法的参数上，所以构造、普通方法的绑定声明不易实现
            // 暂时利用@Resource注解上的mappedName属性，用作需要绑定的参数序号，默认0表示第一个参数
            for (Method method : ReflectUtil.getAccessibleMethods(type)) {
                Resource resOnMethod = method.getAnnotation(Resource.class);
                if (resOnMethod != null) {
                    Integer paramIndex = 0;
                    if (StringUtils.isEmpty(resOnMethod.mappedName())) {
                        paramIndex = 0;
                    } else {
                        paramIndex = Integer.valueOf(resOnMethod.mappedName());
                    }
                    Class paramType = method.getParameterTypes()[paramIndex];
                    addResourceToBindMap(resOnMethod, paramType);
                }
            }
        }
    }

    private void addResourceToBindMap(Resource res, Class type) {
        Preconditions.checkArgument(type != null);
        if (null != res.type() && Object.class != res.type()) {
            // 显式绑定
            if (StringUtils.isEmpty(res.name())) {
                bindMap.put(type, res.type());
                logger.info(String.format("Binding [%s] to [%s].", type.getSimpleName(), res.type()
                        .getName()));
            } else {
                bindMap.put(type, Key.get(res.type(), Names.named(res.name())));
                logger.info(String.format("Binding [%s] named with ['%s'] to [%s].",
                        type.getSimpleName(), res.name(), res.type().getName()));
            }
        } else {
            // 隐式绑定(在interface类上使用@Resrouce可以起到@ImplementedBy的效果)
            // https://github.com/google/guice/wiki/JustInTimeBindings
            bindMap.put(type, null);
            logger.debug(String.format("No implement declared for [%s], Guice will do JIT binding.",
                    type.getSimpleName()));
        }
    }

    /**
     * 实现自动简单绑定的默认Guice Module.
     */
    protected class DefaultAutoBindingModule extends AbstractModule {
        @Override
        public void configure() {
            for (Entry<Class, Object> entry : bindMap.entrySet()) {
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
            if (moduleCallback != null) {
                moduleCallback.configure(binder());
            }
        }
    }

    /**
     * 在默认Module执行完之后会被调用的回调，可以做更多配置操作.
     */
    public interface ConfigureModuleCallback {
        /**
         * 和 {@link Module#configure(Binder)} 一样.
         */
        void configure(Binder binder);
    }

}
