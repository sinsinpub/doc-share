doc-share
=========

Simple Web App for quickly sharing small files & document snippets.

## How to start

* 签出源代码

```
git clone this-repo.git
```

* 进入this-repo目录，构建、打包，生成`doc-share.war`

```
mvn package
```

* 初始化数据库表结构

```
mvn test -Dtest=com.github.sinsinpub.doc.web.model.manual.InitDbTables
```

* 启动内嵌的jetty服务

```
java -jar target\doc-share.war {port} {context} {sslPort}
```

* 如果没有加port等参数并且顺利启动，浏览器打开`http://localhost/`即可Enjoy!

* 由于默认的port 80可能没有权限监听，如果提示被占用则换一个大于1024的端口号(如8080)即可。

* 在没有Maven的环境下也可以添加参数运行war包完成初始化数据库。重复运行不影响已经存在的db。如需重置，删除db文件夹即可。

```
java -DcreateDb=true doc-share.war
```

* 要以开发调试状态启动jetty只需(port默认就是8080)

```
mvn jetty:run
```

* 要配置应用运行时参数可以编辑`conf/runtime.properties`文件。默认属性文件可以这样得到

```
mkdir conf
cp src/main/resources/app-default.properties conf/runtime.properties
```

* 日志文件的位置、保留数、输出等级等简单参数可以编辑`conf/log.properties`文件。默认文件也可以

```
cp src/main/resources/log.properties conf/
```

* `src/main/resources/deploy`目录下有作为服务启动的示例脚本和配置。可以把它们复制到WAR包所在位置使用

```
doc-share.sh start/stop/restart/status
```

* 启用HTTPS
    * 将自己的证书用`keytool`工具导入到JKS格式文件，放到`conf/keystore.jks`。详细可参考[Jetty文档](https://www.eclipse.org/jetty/documentation/current/configuring-ssl.html)。
    * 启动服务之前将keystore的密码配置到适当位置，例如`JAVA_OPTS`的`-Dorg.eclipse.jetty.ssl.keypassword=`(可加混淆)。
    * 启动服务时只要指定了SSL加密端口号即可启用HTTPS。端口号可以在命令行参数上指定，也可以添加`-Dorg.eclipse.jetty.ssl.port=`来配置。
    * 可以复制使用项目带的`src/main/resources/deploy/keystore.jks`进行快速测试，密码不用配置。

## Developer Guide

* 了解项目构成和包作用：其实看源码最直接，也可以生成javadoc来阅读：

```
mvn javadoc:jar
```

* 添加更多功能、依赖：直接改pom和相应的包就行了，这项目现在就是个骨架和示例而已。

* 前端想要写更少、做更多：直接把coffeescript、lesscss之类的引进来即可。one-page类的MVVM框架只要修改后端与之配合也行。

* 后端想要使用正规的数据库：引入相应的驱动、把JDBC相关的配置改了即可。

## License

ASL 2.0
