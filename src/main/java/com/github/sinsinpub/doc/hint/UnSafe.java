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
 * 被本注解标记的类、字段、方法是需要提醒开发者注意：它存在已知的安全问题，需要小心使用。
 * <p>
 * 在这里我们特意使用这个注解来声明内容不安全或存在安全隐患，只为提醒后人，以儆效尤。但不并能因为可能不安全就放弃使用，因咽废食。
 * <p>
 * 类型参数可以有但不限于：
 * <ul>
 * <li><code>confidentiality</code> : 保密性问题。例如权限可能被提升、校验可以被绕过。</li>
 * <li><code>integrity</code> : 完整性问题。例如数据存在全部或部分被破坏、篡改的可能。</li>
 * <li><code>availability</code> : 可用性问题。例如功能有可能被拒绝服务攻击。</li>
 * <li><code>security</code> : 通常以上三种综合起来称为security问题。</li>
 * <li><code>resources-leak</code> : 资源泄露问题。例如内存、句柄可能存在无限被分配却没被释放。</li>
 * <li><code>concurrent-consistency</code> : 并发一致性问题。例如在并发操作存在时，结果会和预期不一样。</li>
 * </ul>
 * <p>
 * “安全”是个很粗泛的概念，可以不客气地断言，所有不加声明的内容其实都可以认为是“不安全”的，只是安全问题还未被发现而已。<br>
 * 能这么说是因为“安全”也是个相对概念，满足保密性、完整性、可用性为之安全，而这三者无一不是需要确定参照物和相关作用域才能成立的概念。<br>
 * 所以我们才有很多相关的，在某些条件下“确定安全”的注解，可以参考下面See Also引用。<br>
 * 没有被本注解标记的内容并不代表就安全。而被标记为某种类型安全的内容也不代表在其它情况下也一样安全。
 * <p>
 * 
 * @see ThreadSafe
 * @see NotThreadSafe
 * @see ExceptionSafe
 * @see NullSafe
 * @author sin_sin
 * @since 1.0.0
 * @version $Date: 2014-04-09 $
 */
@Documented
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.CLASS)
public @interface UnSafe {
    String type();
}
