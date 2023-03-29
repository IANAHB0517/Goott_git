<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>관리자 페이지</title>
</head>
<body>
	<c:set var="contextPath" value="<%=request.getContextPath()%>" />
	<c:if test="${sessionScope.loginMember.isAdmin == 'N' }">
		<jsp:forward page="../member/login.jsp"></jsp:forward>
	</c:if>
	<h1>관리자 페이지</h1>
</body>
</html>