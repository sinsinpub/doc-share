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
 * 一个类(JavaBean组件)被注解为“会话安全的”(session-safe)， 这意味着不会因为有多个会话(例如HTTP
 * Session，TCP连接Session)在短时间类连续的访问该类某一个实例而导致该实例的状态、数据发生异常。
 * <p>
 * 这个情况和{@link ThreadSafe} 很相似，但它是基于会话Session进行隔离，而不是线程Thread。
 * 由于线程是操作系统概念，有很多现成的安全操作办法可以借助操作系统功能来实现。
 * 而会话大多是应用系统通信、交换消息用的概念，往往需要专门编写实现来达到会话间信息隔离的目的。
 * 
 * @see ThreadSafe
 * @author sin_sin
 * @since 1.0.0
 * @version $Date: 2015-04-10 $
 */
@Documented
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.CLASS)
public @interface SessionSafe {

}
