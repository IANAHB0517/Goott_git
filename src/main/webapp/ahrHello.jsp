<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ page import="com.miniproj.member.dao.DBConnection" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<title>ahr</title>
</head>
<body>
	<h1>hi</h1>
	 <% out.println(DBConnection.dbConnect()); %>
</body>
</html>