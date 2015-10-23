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
 * `null-safe`指代对一个方法的传入参数满足能够安全处理<code>null</code>引用不会抛出异常(如@{code
 * NullPointerException})，或者方法的传出返回值能够保证不会返回 <code>null</code>引用，满足这些情况的一种API约束。
 * 
 * @see ExceptionSafe
 * @see ThreadSafe
 * @author sin_sin
 * @since 1.0.0
 * @version $Date: 2015-03-30 $
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface NullSafe {

    /**
     * @return 默认为true，表示对输入的参数引用能够处理好null情况，不会抛出NPE。否则则标记为false
     */
    boolean inParams() default true;

    /**
     * @return 默认为false。如果返回的引用绝对不会出现null则标记为true
     */
    boolean outReturn() default false;
}
