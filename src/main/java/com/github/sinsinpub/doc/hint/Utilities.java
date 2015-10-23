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
 * 在Java世界中`utils`很多情况就指那种集合了静态的(static)、单例的、线程安全快速小工具方法的工具类。
 * <p>
 * 要使编写的工具类符合这些要求，常用方法有：
 * <li>使用<code>abstract</code>修饰类，令类不可被实例化。(Java8以后可以尝试interface的default方法)
 * <li>将默认空构造方法用<code>private</code>修饰，同样令类不可被外部实例化。
 * <li>所有方法都只能是<code>static</code>的。类中最多只定义常量，不应有静态以外的属性字段被定义。
 * <p>
 * 这样写出来的工具类就能保证单例(其实是无实例)、基本线程安全(视是否存在方法去调用别的非线程安全组件)。
 * 
 * @see ThreadSafe
 * @see Singleton
 * @author sin_sin
 * @since 1.0.0
 * @version $Date: 2015-03-27 $
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Utilities {

}
