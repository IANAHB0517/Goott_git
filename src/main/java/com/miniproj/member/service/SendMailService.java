package com.miniproj.member.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.simple.JSONObject;

import com.miniproj.etc.SendMail;
import com.miniproj.member.controller.MemberFactory;

public class SendMailService implements MemberService {

	@Override
	public MemberFactory execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		MemberFactory mf = MemberFactory.getInstance();
		
		resp.setContentType("application/json; charset=utf-8"); // json 형식으로 응답
		PrintWriter out = resp.getWriter();
		
		String userEmailAddr = req.getParameter("mailAddr");
		
		String confirmCode = UUID.randomUUID().toString();
		// 인증코드를 세션에 남겨야함 (그래야 ConfirmCodeService 클래스에서 유저가 입력한 코드랑 비교 가능)
		HttpSession ses = req.getSession();
		ses.setAttribute("confirmCode", confirmCode);
		
		System.out.println(confirmCode);
		
		JSONObject json = new JSONObject();
		// 이메일 발송
		try {
			SendMail.send(userEmailAddr, confirmCode);
			json.put("status", "success");
		} catch (MessagingException e) {
			json.put("status", "fail");
		}
		
		out.print(json.toJSONString());
		out.close();
		
		mf.setRedirect(false);

		return mf;
	}

}
