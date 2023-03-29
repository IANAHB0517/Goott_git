package com.miniproj.board.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.miniproj.board.controller.BoardFactory;
import com.miniproj.board.dao.BoardDAO;
import com.miniproj.board.dao.BoardDAOImpl;
import com.miniproj.vodto.BoardVo;

public class BoardModifyService implements BoardService {

	@Override
	public BoardFactory action(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		BoardFactory bf = BoardFactory.getInstance();
		
		bf.setRedirect(false);
		int boardno = Integer.parseInt(req.getParameter("no"));
		String title = req.getParameter("title");
		bf.setWhereisgo("modifyBoard.jsp");
		
		BoardDAO dao = BoardDAOImpl.getInstance();
		String writer = (String) req.getAttribute("writer");
		
		
		
		
		return bf;
	}

}
