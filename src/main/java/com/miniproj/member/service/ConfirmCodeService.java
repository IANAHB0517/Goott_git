package com.miniproj.member.service;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.miniproj.member.controller.MemberFactory;

public class ConfirmCodeService implements MemberService {

	@Override
	public MemberFactory execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String uic = req.getParameter("uic");
		System.out.println(uic);
		
		// 세션에 바인딩 해둔 코드를 꺼내와야 함
		HttpSession ses = req.getSession();
		String confirmCode = (String)ses.getAttribute("confirmCode");
	
		
		resp.setContentType("application/json; charset=utf-8"); // json 형식으로 응답
		PrintWriter out = resp.getWriter();
		
		JSONObject json = new JSONObject();
		if (uic.equals(confirmCode)) {
			// 이메일 인증 성공
			json.put("status" , "success");
		} else {
			// 이메일 인증 실패
			json.put("status" , "fail");
		}
		
		out.print(json.toJSONString());
		out.close();
		
		return null;
	}

}
