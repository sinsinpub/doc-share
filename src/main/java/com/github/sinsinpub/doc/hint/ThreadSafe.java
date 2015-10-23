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
 * 一个类(JavaBean组件)被注解为“线程安全的”(thread-safe)，
 * 这意味着不会因为有多个线程在短时间类连续的访问该类某一个实例而导致该实例的状态、数据发生异常。
 * 这些访问者不论如何读、写该实例的公开成员，调用公开方法，都无需使用同步锁等机制，来保证实例中的数据、状态保持一致，不会混乱。
 * <p>
 * Based on code developed by Brian Goetz and Tim Peierls and concepts published
 * in 'Java Concurrency in Practice' by Brian Goetz, Tim Peierls, Joshua Bloch,
 * Joseph Bowbeer, David Holmes and Doug Lea.
 * 
 * @see NotThreadSafe
 * @author sin_sin
 * @since 1.0.0
 * @version $Date: 2014-04-29 $
 */
@Documented
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.CLASS)
public @interface ThreadSafe {

}
