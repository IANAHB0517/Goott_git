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

public class BoardDeleteService implements BoardService {

	@Override
	public BoardFactory action(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
//		System.out.println("잘왔나요?");
		
		BoardFactory bf = BoardFactory.getInstance();
		BoardDAO dao = BoardDAOImpl.getInstance();
		
		String writer = req.getParameter("writer");
		int boardno = Integer.parseInt(req.getParameter("no"));
		System.out.println(writer + boardno + "번 글을 삭제하자!");
		
		try {
			if(dao.deletePost(writer, boardno)==1) {
				bf.setRedirect(false);
				bf.setWhereisgo("listAll.bo");
			}
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return bf;
	}

}
