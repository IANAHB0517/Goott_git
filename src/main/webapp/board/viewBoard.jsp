<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시판 상세 조회 페이지</title>
<script>
	function goReply() {
		// 로그인이 되어 있다면 아래의 코드가 콘솔에 출력됨
		console.log('${sessionScope.loginMember.memo}');
		
		
		// 로그인이 되어 있는지 확인
		let loginId = '${sessionScope.loginMember.userId}';
		if (loginId != '') { // 로그인 함
			let url = 'replyBoard.jsp?pNo=${requestScope.board.no }&pRef=${requestScope.board.ref}';
			url += '&pStep=${requestScope.board.step}&pRefOrder=${requestScope.board.reforder}';
			
			location.href = url;
			
		} else { // 로그인 하지 않음
			alert("답글을 작성하시려면 로그인을 해야 합니다");
			location.href='../member/login.jsp';
		}
	}
</script>
</head>
<body>

	<c:set var="contextPath" value="<%=request.getContextPath()%>" />
	<jsp:include page="../header.jsp"></jsp:include>
	<div class="container">
		<h4 style="margin-top: 5px;">게시판 상세 조회 페이지</h4>

		<div class="mb-3 mt-3">
			<label for="writer">번 호 : </label> <input type="text"
				class="form-control" id="no" value="${requestScope.board.no }"
				readonly />
		</div>

		<div class="mb-3 mt-3">
			<label for="writer">글쓴이 : </label> <input type="text"
				class="form-control" id="writer"
				value="${requestScope.board.writer }" readonly />
		</div>

		<div class="mb-3 mt-3">
			<label for="title">작성일 : </label> <input type="text"
				class="form-control" id="postDate"
				value="${requestScope.board.postDate }" readonly />
		</div>

		<div class="mb-3 mt-3">
			<label for="title">조회수 : </label> <input type="text"
				class="form-control" id="title"
				value="${requestScope.board.readcount }" readonly /> <label
				for="title">좋아요 : </label> <input type="text" class="form-control"
				id="title" value="${requestScope.board.likecount }" readonly />
		</div>

		<div class="mb-3 mt-3">
			<label for="title">제 목 : </label> <input type="text"
				class="form-control" id="title" value="${requestScope.board.title }"
				readonly />
		</div>

		<div class="form-check">
			<label for="content">본 문 :</label>
			<div>${requestScope.board.content }</div>
		</div>

		<div class="form-check">

			<c:if test="${requestScope.board.imgFile != '' }">
				<div>
					<img src="${contextPath }/${requestScope.board.imgFile}" />
				</div>
			</c:if>
			
		</div>

		<div class="btns">
			<button type="button" class="btn btn-danger" onclick="">수정</button>
			<button type="button" class="btn btn-success">삭제</button>
			<button type="button" class="btn btn-info" onclick="goReply();">답글달기</button>
			<button type="button" class="btn btn-warning"
				onclick="location.href='listAll.bo';">목록으로</button>
		</div>
		</form>
	</div>
	<jsp:include page="../footer.jsp"></jsp:include>

</body>
</html>