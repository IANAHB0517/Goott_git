package com.miniproj.member.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.miniproj.member.controller.MemberFactory;
import com.miniproj.member.dao.MemberDAO;
import com.miniproj.member.dao.MemberDAOImpl;
import com.miniproj.vodto.MemberDTO;
import com.miniproj.vodto.MemberPointVo;

public class MyPageMemberService implements MemberService {

	@Override
	public MemberFactory execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		
		//  마이페이지
		
		// 파라메터에서 얻기
//		String userId = req.getParameter("userId");
		
		// 세션에서 얻기
		HttpSession ses = req.getSession();
		MemberDTO loginMember = (MemberDTO)ses.getAttribute("loginMember");
		if (loginMember != null) {
			String userId = loginMember.getUserId();
			
			MemberDAO dao = MemberDAOImpl.getInance();
			
			try {
				MemberDTO memberInfo = dao.getMemberInfo(userId); // 회원정보 가져오기
				
				System.out.println(memberInfo.toString());
				
				// 회원 정보와 포인트 내역을 request에 바인딩
				req.setAttribute("memberInfo", memberInfo);

				
				// 페이지 이동
				req.getRequestDispatcher("myPage.jsp").forward(req, resp);
				
				
			} catch (NamingException | SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} 
		
		
		
		return null;
	}


}
