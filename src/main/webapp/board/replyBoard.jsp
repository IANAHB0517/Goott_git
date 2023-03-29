<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 답글 쓰기</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
<script>
	
</script>

<style>
.btns  {
	float :right;
	margin-right: 10px;
}


</style>
</head>
<body>
	<c:if test="${sessionScope.loginMember == null }">
		<c:redirect url="../member/login.jsp"></c:redirect>
	</c:if>
	<jsp:include page="../header.jsp"></jsp:include>
	<div class="container">
		<h4 style="margin-top: 5px;">게시판 답글 쓰기 페이지</h4>

		<form method="post" action="reply.bo">
			<!-- 답글 처리시 필요한 부모글의 데이터를 함께 넘겨준다 -->
			<input type="hidden" name="pNo" value="${param.pNo }" />
			<input type="hidden" name="pRef" value="${param.pRef }" />
			<input type="hidden" name="pStep" value="${param.pStep }" />
			<input type="hidden" name="pRefOrder" value="${param.pRefOrder }" />
			
			<div class="mb-3 mt-3">
				<label for="writer">글쓴이 : </label> <input type="text"
					class="form-control" id="writer" name="writer"
					value="${sessionScope.loginMember.userId }" readonly />
			</div>

			<div class="mb-3 mt-3">
				<label for="title">제 목 : </label> <input type="text"
					class="form-control" id="title" name="title" />
			</div>

			<div class="form-check">
				<label for="content">본 문 :</label>
				<textarea class="form-control" rows="20" id="content" name="content"></textarea>
			</div>

			<div class="btns">
				<button type="button" 
				class="btn btn-danger" onclick="location.href='viewBoard.bo?no=${param.no}';">취소</button>
				<button type="submit" class="btn btn-success">저장</button>
			</div>
		</form>
	</div>
	<jsp:include page="../footer.jsp"></jsp:include>

</body>
</html>