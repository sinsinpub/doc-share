<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="zh">
<head>
<meta charset="utf-8" />
<title>JSP Test</title>
</head>
<body>
<header>
<div id="header"></div>
</header>
<section>
<div id="body"><c:out
	value="This is ${pageContext.request.contextPath}${pageContext.request.requestURI}" /></div>
</section>
<footer>
<div id="footer"></div>
</footer>
</body>
</html>
