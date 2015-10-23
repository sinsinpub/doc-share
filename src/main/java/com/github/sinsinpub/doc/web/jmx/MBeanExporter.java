package com.github.sinsinpub.doc.web.jmx;

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
 * 作为jfinal的Plugin，顺便管理生命周期结束时的自动注销行为。
 * 
 * @author sin_sin
 * @version $Date: Oct 23, 2015 $
 */
public class MBeanExporter implements IPlugin {

    public static MBeanExporter INSTANCE = new MBeanExporter();
    private static final Logger LOG = Logger.getLogger(MBeanExporter.class);
    protected final Set<String> registeredObjectNames = new HashSet<String>();

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
            Management.register(mbean, objectName);
            registeredObjectNames.add(objectName);
        } catch (InstanceAlreadyExistsException aee) {
            try {
                Management.unregister(objectName);
                Management.register(mbean, objectName);
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
            Management.unregister(objectName);
            registeredObjectNames.remove(objectName);
        }
    }

    /**
     * 批量注销所有经过这里导出的MBean.
     */
    public void unexportAllRegistered() {
        for (String name : registeredObjectNames) {
            try {
                unexport(name);
            } catch (ManagementException e) {
                LOG.warn(String.format("Failed on unregistering %s: %s", name, e.toString()));
            }
        }
        registeredObjectNames.clear();
    }

    @Override
    public boolean start() {
        return true;
    }

    @Override
    public boolean stop() {
        unexportAllRegistered();
        return true;
    }

    /**
     * 修改默认实例.
     * 
     * @param instance
     */
    public static void setDefaultInstance(MBeanExporter instance) {
        INSTANCE = instance;
    }

}
