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
 * `exception-safe`指一个方法在调用过程中不但不会会抛出`Checked Exception`(如果没有声明的话)，也不会抛出`Unchecked
 * Exception`这样一种API约束。这通常意味着该方法连@{code {@link RuntimeException}
 * 类的异常也拦截并很好的处理了。这其实大多用在封装JDK或其它三方API的方法时，对被封装方法可能会抛出的异常也进行封装处理之后，
 * 让自己的调用者不再需要关心异常处理的一种封装办法。
 * 
 * @see NullSafe
 * @author sin_sin
 * @since 1.0.0
 * @version $Date: 2015-03-30 $
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface ExceptionSafe {

}
