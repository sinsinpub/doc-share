doc-share
=========

Simple Web App for quickly sharing small files & document snippets.

## How to start

* 签出源代码

```
git clone this-repo.git
````

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
java -jar target\doc-share.war {port} {context}
```

* 如果没有加port等参数并且顺利启动，浏览器打开`http://localhost/`即可Enjoy!
* 由于默认的port 80可能没有权限监听，如果提示被占用则换一个大于1024的端口号(如8080)即可。

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

## Developer Guide

TBD

## License

APL 2.0
