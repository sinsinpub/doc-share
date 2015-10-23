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
 * 与“线程安全的”(thread-safe)相对的是“线程不安全”，一个类确认它当中包含了需要同步维护一致性状态的成员属性，或引用了其它非线程安全的组件，
 * 则使用本注解来明确这一点。
 * 通常来说我们不应当假设一个安全性未知的类就是“线程安全的”，但也不能确认它就是“线程不安全”，这类情况我们优先认为它是“非线程安全的”。
 * <p>
 * Based on code developed by Brian Goetz and Tim Peierls and concepts published
 * in 'Java Concurrency in Practice' by Brian Goetz, Tim Peierls, Joshua Bloch,
 * Joseph Bowbeer, David Holmes and Doug Lea.
 * 
 * @see ThreadSafe
 * @author sin_sin
 * @since 1.0.0
 * @version $Date: 2014-04-29 $
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface NotThreadSafe {

}
