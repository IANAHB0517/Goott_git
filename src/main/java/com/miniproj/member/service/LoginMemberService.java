package com.miniproj.member.service;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.miniproj.error.CommonException;
import com.miniproj.member.controller.MemberFactory;
import com.miniproj.member.dao.MemberDAOImpl;
import com.miniproj.vodto.LoginDTO;
import com.miniproj.vodto.MemberDTO;

public class LoginMemberService implements MemberService {

	@Override
	public MemberFactory execute(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		
		MemberFactory mf = MemberFactory.getInstance();

		String userId = req.getParameter("userId");
		String pwd = req.getParameter("pwd");
		
		LoginDTO dto = new LoginDTO(userId, pwd);
		System.out.println(dto.toString());
		
		try {
			MemberDTO loginMember = MemberDAOImpl.getInance().loginWithTransaction(dto);
			if (loginMember !=  null) { // 로그인 성공
				
				// 로그인한 유저의 정보를 세션객체에 바인딩
				HttpSession ses =  req.getSession();
				ses.setAttribute("loginMember", loginMember);
				
				mf.setRedirect(true);
				mf.setWhereisgo("../index.jsp");

			} else {
				mf.setRedirect(true);
				mf.setWhereisgo("login.jsp?status=fail");
			}
		} catch (NamingException | SQLException e) {
			if (e instanceof NamingException) {
				// NamingException은 개발자 실수이기 때문에 개발자만 보도록 공통 에러페이지(error.jsp)를 만들었고
				// 에러 정보를 error.jsp로 바인딩하여 error.jsp페이지에서 에러 정보를 출력하였다.
				// forward
				
				CommonException ce = new CommonException(e.getMessage(), 99);
//				throw ce; // 강제로 예외를 발생 시킴
				
				ce.setErrorMsg(e.getMessage());
				ce.setStackTrace(e.getStackTrace());
				
				req.setAttribute("error", ce);  // 에러 정보를 가진 CommonException 객체 바인딩
				
				req.getRequestDispatcher("../error.jsp").forward(req, resp);  // 페이지 이동
				
			} else if (e instanceof SQLException) {
				// SQL Exception 은 대부분 실제 유저의 입력 오류로 인한 예외
				mf.setRedirect(true);
				mf.setWhereisgo("login.jsp?status=fail");
			}
		}
				

		return mf;
	}

}
