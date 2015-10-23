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
 * 一个目标被定义为SPI(Service Provider Interface,服务提供者接口)意味着它将是
 * 能够以后<b>被开发者扩展</b>，为了<b>实现某种功能</b>的公开接口。<br>
 * SPI用于告诉别的开发者(大多为自然人)这段目标程序应该怎么做，要遵守什么约定。
 * <p>
 * 声明为SPI可以理解为这段程序被设计者定义为“扩展点”，以后这里可以按照定义的约定有序扩展；
 * API则是程序设计者定义的“功能点”，为了告诉别人这里有这样的现成功能可以使用。<br>
 * SPI和API大多会被自然区分开来，例如JDBC驱动本身是SPI，驱动使用者并不会直接去调用驱动的任何接口；而驱动开发者根据统一的约定
 * 为不同的数据库实现不同的具体驱动方法。<br>
 * 不过也存在同时既为API也是SPI的情况，例如JDBC的{@link java.sql.Connection}，
 * 开发者使用它的API与数据库进行连接，另一方面驱动的开发者按照它的SPI约定实现特定数据库相应的连接方法。
 * 
 * @see API
 * @author sin_sin
 * @since 1.0.0
 * @version $Date: 2015-01-21 $
 */
@Documented
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.CLASS)
public @interface SPI {

}
