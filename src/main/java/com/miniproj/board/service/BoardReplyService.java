package com.miniproj.board.service;

import java.io.IOException;
import java.sql.SQLException;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.miniproj.board.controller.BoardFactory;
import com.miniproj.board.dao.BoardDAO;
import com.miniproj.board.dao.BoardDAOImpl;
import com.miniproj.error.CommonException;
import com.miniproj.vodto.BoardVo;

public class BoardReplyService implements BoardService {

	@Override
	public BoardFactory action(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		BoardFactory bf = BoardFactory.getInstance();
		
		String writer = req.getParameter("writer");
		String title = req.getParameter("title");
		String content = req.getParameter("content");
		
		int pNo = Integer.parseInt(req.getParameter("pNo"));
		int pRef = Integer.parseInt(req.getParameter("pRef"));
		int pStep = Integer.parseInt(req.getParameter("pStep"));
		int pRefOrder = Integer.parseInt(req.getParameter("pRefOrder"));
		
//		System.out.println("잘나오나" + writer + title + content + pNo + pRef + pStep + pRefOrder);
		
		content = content.replace("\n", "<br/>");
		
		BoardVo reply = new BoardVo(0, writer, title, null, content, null, 0, 0, pRef, pStep, pRefOrder);
		System.out.println(reply.toString());
		
		BoardDAO dao = BoardDAOImpl.getInstance();
		try {
			if(dao.transactionProcessForReplyPost(reply) == 1) {
				bf.setRedirect(true);
				bf.setWhereisgo("listAll.bo");
			} else {
				bf.setRedirect(true);
				bf.setWhereisgo("viewBoard.bo?no=" + pNo + "&statu=fail");
			}
		} catch (NamingException | SQLException e) {
			if(e instanceof NamingException) {
				// NamingException은 개발자 실수이기 때문에 개발자만 보도록 공통 에러페이지(error.jsp)를 만들었고,
				// 에러 정보를 error.jsp로 바인딩하여 error.jsp페이지에서 에러 정보를 출력하였다.
				// forward
				CommonException ce = new CommonException(e.getMessage(), 99);
				// throw ce; // 강제로 예외를 발생 시킴
				
				ce.setErrorMsg(e.getMessage());
				ce.setStackTrace(e.getStackTrace());
				
				req.setAttribute("error", ce); // 에러 정보를 가진 CommonException 객체 바인딩
				
				bf.setRedirect(false);
				bf.setWhereisgo("../error.jsp");				
			} else if(e instanceof SQLException) {
				bf.setRedirect(true);
				bf.setWhereisgo("viewBoard.bo?no=" + pNo + "&statu=fail");
			}
		}
		
		return bf;
	}

}
