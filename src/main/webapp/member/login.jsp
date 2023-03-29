<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인 페이지</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
<script src="../js/commonJS.js"></script>
<script>
$(document).ready(function(){
	if(getParameter("status") === "fail") {
		alert("로그인 실패!");
	}
});
</script>
</head>
<body>
	<jsp:include page="../header.jsp"></jsp:include>
	<div class="container">
		<h1>login.jsp</h1>
		
		<form method="post" action="login.mem">
			<div class="mb-3 mt-3">
				<label for="userId">아이디:</label> <input type="text"
					class="form-control" id="userId" placeholder="Enter your name"
					name="userId" />
				<div class='errMsg'></div>
			</div>

			<div class="mb-3">
				<label for="pwd">Password:</label> <input type="password"
					class="form-control" id="pwd1" placeholder="Enter password"
					name="pwd" />
				<div class='errMsg'></div>
			</div>
			<button type="submit" class="btn btn-success">로그인</button>
			<button type="reset" class="btn btn-warning">취소</button>
		</form>
	</div>
	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>