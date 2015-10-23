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
 * 一个类(通常为数据容器JavaBean)被注解为“不可变”(immutable)时，表示它的某个实例存储的数据在被赋与初值之后就不会再“变化”，
 * 变化包括引用本身不会改变以及引用所指向的数据值不会改变。通常来说它应该有：
 * <ul>
 * <li>所有公开成员域都是final的；</li>
 * <li>所有公开引用指向的也都是不可变的对象；</li>
 * <li>实现时构造和方法都不能暴露出指向内部有可能改变状态的引用；</li>
 * </ul>
 * 为了优化性能，不可变容器内部会有可变的或延迟初始化、计算的内容。虽说某个实例的内容是“不可变”的，但这并不意味着它只能当成“常量”使用，
 * 因为内容变化后产生的新实例通常可以被用来替代旧的 ，将新引用赋回给原来的变量。
 * <p>
 * 在Java中所有基本类型的封包类(如Integer、Long)还有常用的String等都是不可变组件。而相对可变组件有：AtomicInteger、Date、
 * StringBuilder等。与传统编程语言不同，基于函数式的编程语言将所有数据类型都定义为不可变的。
 * <p>
 * 可变对象由于内部含有可以公开的状态及数据，在并发编程时往往需要关注是否为“线程安全的”。<br>
 * 而不可变对象通常可以直接认为是线程安全的，它们可以被公开在不同线程之间传递， 而无需进行同步控制。
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
public @interface Immutable {

}
