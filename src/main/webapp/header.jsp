<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
	rel="stylesheet">

<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
	
<style>
	.userImg {
		width : 100px;
		
		
	} 
</style>
</head>
<body>
	<c:set var="contextPath" value="<%=request.getContextPath()%>" />
	<nav class="navbar navbar-expand-sm navbar-dark bg-dark">
		<div class="container-fluid">
			<a class="navbar-brand" href="${contextPath }/index.jsp">Home</a>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#mynavbar">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="mynavbar">
				<ul class="navbar-nav me-auto">
					<li class="nav-item"><a class="nav-link"
						href="member/register.jsp">회원가입</a></li>

					<li class="nav-item"><a class="nav-link"
						href="${contextPath }/board/listAll.bo">게시판</a></li>

					<c:choose>
						<c:when test="${sessionScope.loginMember != null }">
							<!-- 로그인 성공 -->
							<li class="nav-item"><a class="nav-link"
								href="${contextPath }/member/logout.mem">로그아웃</a></li>
							
							<li class="nav-item"><a class="nav-link"
								href="${contextPath }/member/myPage.mem?userId=${sessionScope.loginMember.userId }">
								<img class="userImg" src="${contextPath }/${sessionScope.loginMember.userImg }" />
								${sessionScope.loginMember.userId }
								</a></li>
								
							<li class="nav-item"><a class="nav-link"
								href="${contextPath }/admin/admin.jsp">관리자페이지</a></li>
						</c:when>
						<c:otherwise>
							<li class="nav-item"><a class="nav-link"
								href="${contextPath }/member/login.jsp">로그인</a></li>
						</c:otherwise>
					
					</c:choose>
					
				</ul>
				<form class="d-flex">
					<input class="form-control me-2" type="text" placeholder="Search">
					<button class="btn btn-primary" type="button">Search</button>
				</form>
			</div>
		</div>
	</nav>
</body>
</html>