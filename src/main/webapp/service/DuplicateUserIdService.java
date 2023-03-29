package com.miniproj.member.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.google.gson.JsonObject;
import com.miniproj.error.CommonException;
import com.miniproj.member.controller.MemberFactory;
import com.miniproj.member.dao.MemberDAOImpl;

public class DuplicateUserIdService implements MemberService {

	@Override
	public MemberFactory execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		MemberFactory mf = MemberFactory.getInstance();
		
		resp.setContentType("application/json; charset=utf-8"); // json 형식으로 응답
		PrintWriter out = resp.getWriter();
		
		String userId = req.getParameter("userId");
		System.out.println("서비스 단 : " + userId + "값이 중복되는지 검사하자");
		
		JSONObject json = new JSONObject();
		
		try {
			int result = MemberDAOImpl.getInance().selectByUserId(userId);
			System.out.println(result);
			
			json.put("status", "success");
			if (result == 0) { // 중복 x
				json.put("duplicate", "no");
			} else { // 중복 o
				json.put("duplicate", "yes");
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
				
			} else {
				json.put("status", "fail");
			}
			
		}
		
		out.print(json.toJSONString());
		out.close();
		
		mf.setRedirect(false); // 리다이렉트 안할꺼다
		
		return mf;
	}

}
