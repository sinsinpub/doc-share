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

* 启动Jetty服务

```
java -jar target\doc-share.war [port] [context]
```

* 如果没有加port等参数并且顺利启动，浏览器打开`http://localhost/`即可Enjoy!
