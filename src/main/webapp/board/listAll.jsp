<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
<script>
	function goViewBoard(no) {
		location.href='viewBoard.bo?no=' + no;
	}
	
	function getVpc() {
		let vpc = Number('${param.viewPost}');
		if (vpc == '') {
			vpc = 3;
		} 
		
		$(".viewPostCnt").val(vpc); // select 태그의 value값 변경
	}
	
	
	function searchValid() {  
		// 검색어가 입력 되지 않거나, sql injection 공격요소가 있다면 검색이 되지 않도록
				
		let sw = $("#searchWord").val();
		if (sw.length == 0) {
			alert("검색어를 입력하세요!");
			return false;
		} 
		
		
		let expText = /[%=><]/;  // 데이터베이스에서 조건연산자에 해당
		if (expText.test(sw) == true) {
			alert("특수문자를 입력 할 수 없습니다.");
			return false;
		} 
		
		const sql = new Array(
			"or", "select", "insert", "update", "delete", "create", "alter", "drop", "exec", 
			"union", "fetch", "declare", "truncate"
		);
		
		let regEx = "";
		for (let i = 0; i < sql.length; i++) {
			regEx = new RegExp(sql[i], "gi");
			
			if (regEx.test(sw) == true) {
				alert("특정 문자로 검색할 수 없습니다");
				return false;
			}
		}
		
		
		
		return true;
	}
	
	
	$(function(){
		getVpc();
		
		$(".viewPostCnt").change(function() {
			let vpc = $(this).val();
			
			location.href="listAll.bo?pageNo=${param.pageNo}&viewPost=" + vpc;
		});
	});
	
</script>
<style>
.board {
	margin-top: 15px;
	margin-bottom: 15px;
}

.writeBtn {
	float: right;
	margin-right: 10px;
}

.paging {
	clear: both;
}

.replyImg {
	width: 20px;
}
</style>
</head>
<body>
	<c:set var="contextPath" value="<%=request.getContextPath()%>" />
	<c:if test="${requestScope.boardList == null}">
		<c:redirect url="listAll.bo"></c:redirect>
	</c:if>

	<jsp:include page="../header.jsp"></jsp:include>
	<div class="container">
		<h4 style="margin-top: 5px;">게시판 글 목록 페이지</h4>

		<div>
			<select class="viewPostCnt">
				<option value="3" selected>3개씩보기</option>
				<option value="5">5개씩보기</option>
				<option value="10">10개씩보기</option>
				<option value="20">20개씩보기</option>
			</select>
		</div>

		<div class="board">
			<table class="table table-hover">
				<thead>
					<tr>
						<th>글번호</th>
						<th>제 목</th>
						<th>글쓴이</th>
						<th>글쓴날짜</th>
						<th>조회수</th>
						<th>좋아요</th>
						<th>ref</th>
						<th>step</th>
						<th>reforder</th>

					</tr>
				</thead>
				<tbody>
					<c:forEach var="board" items="${requestScope.boardList }">
						<tr onclick="goViewBoard(${board.no });">
							<td>${board.no }</td>


							<td><c:if test="${board.step > 0}">
									<c:forEach var="i" begin="1" end="${board.step }" step="1">
										<img src="${contextPath }/images/reply.png" class="replyImg" />
									</c:forEach>
								</c:if> ${board.title }</td>

							<td>${board.writer }</td>
							<td>${board.postDate }</td>
							<td>${board.readcount }</td>
							<td>${board.likecount }</td>
							<td>${board.ref }</td>
							<td>${board.step }</td>
							<td>${board.reforder }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>

			<div class="btns">
				<button type="button" class="btn btn-primary writeBtn"
					onclick="location.href='writeBoard.jsp';">글쓰기</button>
			</div>

		
			<form class="searchBoard" action="listAll.bo">
				<select name="searchType">
					<option value="">--검색어 선택--</option>
					<option value="title">제목</option>
					<option value="writer">글쓴이</option>
					<option value="content">본문</option>
				</select>
				
				<input type="text" name="searchWord" id="searchWord" />
				<button type="submit" onclick="return searchValid();">검색</button>
			</form>

		
			<div class="paging">
				<ul class="pagination">
					<c:if test="${requestScope.pagingInfo.startNumOfCurrentPagingBlock > 1 }">
						<li class="page-item"><a class="page-link"
							href="listAll.bo?pageNo=${param.pageNo - 1 }&viewPost=${param.viewPost}&searchType=${param.searchType }&searchWord=${param.searchWord}">Previous</a></li>
							
					</c:if>
					<c:forEach var="i"
						begin="${requestScope.pagingInfo.startNumOfCurrentPagingBlock }"
						end="${requestScope.pagingInfo.endNumOfCurrentPagingBlock}"
						step="1">

						<c:choose>
							<c:when test="${requestScope.pagingInfo.pageNo == i }">
								<li class="page-item active"><a class="page-link"
									href="listAll.bo?pageNo=${i }&viewPost=${param.viewPost}&searchType=${param.searchType }&searchWord=${param.searchWord}">${i }</a></li>
							</c:when>
							<c:otherwise>
								<li class="page-item"><a class="page-link"
									href="listAll.bo?pageNo=${i }&viewPost=${param.viewPost}&searchType=${param.searchType }&searchWord=${param.searchWord}">${i }</a></li>
							</c:otherwise>
						</c:choose>



					</c:forEach>
					<c:if
						test="${param.pageNo <  requestScope.pagingInfo.endNumOfCurrentPagingBlock}">
						<li class="page-item"><a class="page-link"
							href="listAll.bo?pageNo=${param.pageNo + 1 }&viewPost=${param.viewPost}&searchType=${param.searchType }&searchWord=${param.searchWord}">Next</a></li>
					</c:if>
				</ul>
			</div>
		</div>
	</div>


	<jsp:include page="../footer.jsp"></jsp:include>
</body>
</html>