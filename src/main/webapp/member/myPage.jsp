<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이 페이지</title>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.6.3/jquery.min.js"></script>
<script>
	function showBasic() {
		$(".card").show();
		$(".memberPoint").hide();
	}

	function showPoint(pageNo) {
		$(".card").hide();
		
		$.ajax({
			url : "getMemPoint.mem", // 데이터가 송수신될 서버의 주소(서블릿의 매핑주소작성)
			type : "get", // 통신 방식 (GET, POST, PUT, DELETE)
			data : {
				"userId" : "${memberInfo.userId}",
				"pageNo" : pageNo
			}, // 서블릿에 전송할 데이터
			dataType : "json", // 수신받을 데이터 타입(MIME TYPE)
			success : function(data) { // 통신이 성공하면 수행할 함수(콜백 함수)
				console.log(data);

				if (data.status == "success") {
					parsePoints(data);
				} else if (data.status == "fail") {
					alert("잠시 후, 다시 시도해 주세요");
				}
			}
		});
		
		
	}
	
	function parsePoints(json) {
		let output = "<table class='table table-striped'><thead><tr><th>적립 일시</th><th>적립 사유</th>";
		output += "<th>적립금</th></tr></thead><tbody>";
		$.each(json.memberPoints, function(i, item) {
			output += "<tr>";
			output += "<td>" + item.when + "</td>";
			output += "<td>" + item.why + "</td>";
			output += "<td>" + item.howmuch + "</td>";
			output += "</tr>";
		});
		output += "</tbody></table>";
		
		$(".memberPoint").html(output);
		
		let pagination = "<ul class='pagination'>"
		
		for (let i = json.startNumOfCurrentPagingBlock; i <= json.endNumOfCurrentPagingBlock; i++) {
			pagination += "<li class='page-item'><a class='page-link' href='#' onclick='showPoint(" + i + ");'>" + i + "</a></li>";
		}
		
		pagination += "</ul>";
		
		/*
		  <li class="page-item"><a class="page-link" href="#">Previous</a></li>
		  
		  <li class="page-item active"><a class="page-link" href="#">2</a></li>
		  <li class="page-item"><a class="page-link" href="#">3</a></li>
		  <li class="page-item"><a class="page-link" href="#">Next</a></li>
		
		*/
		
		$(".mpPagiNation").html(pagination);
		
		$(".memberPoint").show();
	}
</script>
<style>
.memberPoint {
	display: none;
}
</style>
</head>
<body>
	<c:set var="contextPath" value="<%=request.getContextPath()%>" />
	<jsp:include page="../header.jsp"></jsp:include>
	<div class="container">
		<h4>마이 페이지</h4>

		<ul class="nav nav-tabs">
			<li class="nav-item"><a class="nav-link active" href="#"
				onclick="showBasic();">기본정보</a>
				<div class="card" style="width: 400px; margin: 20px auto;">
					<img class="card-img-top"
						src="${contextPath }/${memberInfo.userImg }"
						alt="${memberInfo.userId }">
					<div class="card-body">
						<h4 class="card-title">${memberInfo.userId }</h4>
						<p class="card-text">
						<div class="userEmail">${memberInfo.userEmail }</div>
						<div class="userMobile">${memberInfo.userMobile }</div>
						<div class="userGender">${memberInfo.userGender }</div>
						<div class="userhobbies">${memberInfo.hobbies }</div>
						<div class="job">${memberInfo.job }</div>
						<div class="memo">${memberInfo.memo }</div>
						</p>
					</div>
				</div></li>
			<li class="nav-item"><a class="nav-link" href="#"
				onclick="showPoint();">포인트 적립내역</a>
				<div class="memberPoint">
				</div>

				<div class="mpPagiNation"></div>				
				</li>
			<!-- <li class="nav-item"><a class="nav-link" href="#">내가 쓴 글</a></li>
			<li class="nav-item"><a class="nav-link disabled" href="#">Disabled</a>
			</li> -->
		</ul>






	</div>
	<jsp:include page="../footer.jsp"></jsp:include>

</body>
</html>