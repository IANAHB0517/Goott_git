package com.miniproj.member.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Server;

import com.miniproj.member.service.MemberService;

@WebServlet("*.mem")  // .mem으로 끝나는 모든 매핑 주소에 대해 현재의 서블릿이 동작한다는 것을 의미
public class MemberServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doService(req, resp);
	}
	
	// GET / POST 어떤 방식으로 호출 되던지, 아래의 메서드가 호출됨
	private void doService(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println(this.getServletName() + " 동작");
		System.out.println("요청한 페이지 : " + req.getRequestURL());
		System.out.println("요청한 URI : " + req.getRequestURI());
		System.out.println("요청한 통신 방식 : " + req.getMethod());
	
		String requestUri = req.getRequestURI();
		String contextPath = req.getContextPath();  // /MiniProject
		
		String command = requestUri.substring(contextPath.length());
		System.out.println("요청된 서비스 : "+ command);
		
		MemberFactory mf = MemberFactory.getInstance();
		MemberService service =  mf.getService(command);
		
		if (service != null) {
			mf = service.execute(req, resp);
		}
		
		if (mf != null && mf.isRedirect()) {
			resp.sendRedirect(mf.getWhereisgo());  // 페이지 이동
		}
		
		return;
		
	}
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doService(req, resp);
	}
	
}
