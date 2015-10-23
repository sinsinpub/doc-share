/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package com.github.sinsinpub.doc.hint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 单例(Singleton,单一实例)表示该类期望在程序运行过程中始终只会产生一个实例。<br>
 * 这通常意味着它需要统一管理一套状态，或者它会占用较多的系统资源，同时它本身需要是线程安全的。
 * <p>
 * 与{@link Prototype}一起，这组声明主要参考了Spring Framework的BeanFactory实例管理思路，概念与spring的一致。
 * 具体的类实现并不一定需要将构造方法置为private，初始化一个自身唯一实例在内部；可以利用像spring这样的容器实现单例的管理。
 * 
 * @see Prototype
 * @see ThreadSafe
 * @author sin_sin
 * @since 1.0.0
 * @version $Date: 2015-01-21 $
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Singleton {

}
