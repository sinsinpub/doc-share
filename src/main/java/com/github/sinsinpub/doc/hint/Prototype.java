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
 * 原型(Prototype,原型范例)表示该类其实只是一类实例的原型、模板，在程序运行过程中可以根据需要产生多个不同实例，每个实例可以有自己的特征。<br>
 * 这通常意味着它管理的内部状态需要存在多种不同的值，每个实例不会占用太多的系统资源，而就某个实例而言一般不是线程安全的。
 * <p>
 * 与{@link Singleton}一起，这组声明主要参考了Spring Framework的BeanFactory实例管理思路，概念与spring的一致。
 * 这种类如果不是在实例的层面上讨论，类似于类型定义层的Abstract类，在它的具体派生中往往通过扩展更多的属性成为真正有用的具体实例，而它们有着共同的原型。
 * 
 * @see Singleton
 * @see NotThreadSafe
 * @author sin_sin
 * @since 1.0.0
 * @version $Date: 2015-01-21 $
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Prototype {

}
