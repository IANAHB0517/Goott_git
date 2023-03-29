package com.miniproj.board.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.miniproj.board.service.BoardService;

@WebServlet("*.bo")  // .bo로 끝나는 모든 매핑 처리
public class BoardServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doService(req, resp);
	}
	
	private void doService(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		System.out.println("BoardServlet!");
		String requestUri = req.getRequestURI();
		String contextPath = req.getContextPath();  // /MiniProject
		
		String command = requestUri.substring(contextPath.length());
		System.out.println("요청된 서비스 : "+ command);
		
		BoardFactory bf = BoardFactory.getInstance();
		
		BoardService service = bf.getService(command);
		
		bf = service.action(req, resp);
		
		if (bf.isRedirect()) { // redirect
			resp.sendRedirect(bf.getWhereisgo());
			
		} else { // forward
			RequestDispatcher rd = req.getRequestDispatcher(bf.getWhereisgo());
			rd.forward(req, resp);
		}
		
	}
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doService(req, resp);
	}
	
}
