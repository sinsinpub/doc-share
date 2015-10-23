package com.github.sinsinpub.doc.web.jmx;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.management.InstanceAlreadyExistsException;

import net.gescobar.jmx.Management;
import net.gescobar.jmx.ManagementException;

import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;

/**
 * 简易编程式MBean导出器.
 * <p>
 * 利用https://github.com/germanescobar/jmx-annotations的工具方便地把JavaBean注解成MBean，
 * 直接把JavaBean实例拿到这里来注册到PlatformMBeanServer。<br>
 * 作为JFinal的Plugin，顺便管理生命周期结束时的自动注销行为。
 * 
 * @author sin_sin
 * @version $Date: Oct 23, 2015 $
 */
public class MBeanExporter implements IPlugin {

    /** 默认导出器实例。可以用自建的覆盖。建议保持单例。 */
    public static MBeanExporter INSTANCE = new MBeanExporter();
    private static final Logger LOG = Logger.getLogger(MBeanExporter.class);
    protected final Set<String> registeredObjectNames = Collections.synchronizedSet(new HashSet<String>());

    /**
     * 新建导出器实例.
     */
    public MBeanExporter() {
    }

    /**
     * 用指定的名称导出指定MBean。如果同名MBean已经注册，会先注销旧的MBean。
     * 
     * @param mbean MBean实例
     * @param objectName MBean名称
     * @throws ManagementException 发生其它注册异常时
     */
    public void export(Object mbean, String objectName) throws ManagementException {
        try {
            LOG.debug(String.format("Registering mbean %s with object name: %s", mbean, objectName));
            Management.register(mbean, objectName);
            getRegisteredObjectNames().add(objectName);
        } catch (InstanceAlreadyExistsException aee) {
            LOG.debug(String.format("MBean %s already registered, will be re-registered", objectName));
            try {
                Management.unregister(objectName);
                Management.register(mbean, objectName);
                getRegisteredObjectNames().add(objectName);
            } catch (Exception e) {
                throw new ManagementException(e);
            }
        }
    }

    /**
     * 注销指定名称的MBean。如果该名称MBean不存在，则不做任何动作。
     * 
     * @param objectName MBean名称
     * @throws ManagementException 发生其它注册异常时
     */
    public void unexport(String objectName) throws ManagementException {
        if (Management.isRegistered(objectName)) {
            LOG.debug(String.format("Unregistering mbean: %s", objectName));
            Management.unregister(objectName);
            getRegisteredObjectNames().remove(objectName);
        } else {
            LOG.debug(String.format("MBean %s not exists, no need to unregister", objectName));
        }
    }

    /**
     * 批量注销所有经过这里导出的MBean.
     */
    public void unexportAll() {
        LOG.debug(String.format("All registered %d mbeans will be unregistered",
                getRegisteredObjectNames().size()));
        Set<String> names = getRegisteredObjectNames();
        for (String name : names) {
            try {
                Management.unregister(name);
            } catch (ManagementException e) {
                LOG.warn(String.format("Unregistering mbean %s exception: %s", name, e));
            }
        }
        names.clear();
    }

    @Override
    public boolean start() {
        // 可以考虑实现一个注解扫描器，在启动时自动导出MBean实例。
        // 但由于没有用IoC容器，实例化的时机和依赖管理并不容易。
        return true;
    }

    @Override
    public boolean stop() {
        unexportAll();
        return true;
    }

    /**
     * @return 当前已经导出了的ObjectName集合
     */
    public Set<String> getRegisteredObjectNames() {
        return registeredObjectNames;
    }

    /**
     * 修改默认实例.
     * 
     * @param instance 另外创建的实例
     */
    public static void setDefaultInstance(MBeanExporter instance) {
        INSTANCE = instance;
    }

}
