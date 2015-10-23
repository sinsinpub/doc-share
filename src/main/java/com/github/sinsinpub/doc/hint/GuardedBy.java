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
 * 在可变线程安全组件中，一个成员属性或方法是否能够保证并发安全通常需要利用排他锁进行同步控制。<br>
 * 使用本注解声明一个属性或方法被另一个以什么命名的排他锁(通常是java.util.concurrent.Lock的实现)来进行了同步控制。
 * <p>
 * 参数可以有：
 * <ul>
 * <li><code>this</code> : 该属性或方法所在的内对象部锁</li>
 * <li><code>class-name.this</code> : 指定类的对象锁，通常用于嵌套内部类。</li>
 * <li><code>itself</code> : 属性所引用的组件本身已经包含锁机制</li>
 * <li><code>field-name</code> : 另一个起排他锁作用的属性名</li>
 * <li><code>class-name.field-name</code> : 另一个类的指定静态属性</li>
 * <li><code>method-name()</code> : 通过某个无参数的方法返回一个锁实例</li>
 * <li><code>class-name.class</code> : 指定类的类定义锁</li>
 * </ul>
 * <p>
 * Based on code developed by Brian Goetz and Tim Peierls and concepts published
 * in 'Java Concurrency in Practice' by Brian Goetz, Tim Peierls, Joshua Bloch,
 * Joseph Bowbeer, David Holmes and Doug Lea.
 * 
 * @author sin_sin
 * @since 1.0.0
 * @version $Date: 2014-04-29 $
 */
@Documented
@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.CLASS)
public @interface GuardedBy {
    String value();
}
