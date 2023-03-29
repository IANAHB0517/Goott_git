package com.miniproj.member.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.miniproj.member.service.MemberService;

/**
 * Servlet implementation class MemberServlet
 */
@WebServlet("*.mem") // .mem으로 끝나는 모든 매핑 주소에 대해 현재의 서블릿이 동작
public class MemberServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public MemberServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doService(request, response);
	}

	// GET , POST 어떤 방식으로 호출 되던지, 아래의 메소드가 호출됨
	private void doService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		System.out.println(this.getServletName() + "동작");
		System.out.println("요청한 페이지 : " + request.getRequestURL());
		System.out.println("요청한 페이지 : " + request.getRequestURI());
		System.out.println("요청한 통신 방식 : " + request.getMethod());

		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();

		String command = requestUri.substring(contextPath.length());
		System.out.println("요청된 서비스 : " + command);

		MemberFactory mf = MemberFactory.getInstance();
		MemberService service = mf.getService(command);

		if (service != null) {
			mf = service.execute(request, response);		
		}
		if (mf != null && mf.isRedirect()) {
			response.sendRedirect(mf.getWhereIsgo()); // 페이지 이동
		}
		return;
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doService(req, resp);
	}

}
