/**
 * 实现用注解式声明MBean定义，辅助注册到MBeanServer的工具.
 * <p>
 * 由于JMX API 2.0实现迟迟不能加入JDK，而暂时不想依赖spring-jmx。<br>
 * 该项目貌似没有发布到Maven中央仓库，所以直接引入了源码，顺便修正几处小错误。<br>
 * 源代码来自：https://github.com/germanescobar/jmx-annotations
 */
package net.gescobar.jmx;

