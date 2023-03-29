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
import com.miniproj.etc.IPCheck;
import com.miniproj.vodto.BoardVo;
import com.miniproj.vodto.ReadCountProcess;

public class BoardViewService implements BoardService {

	@Override
	public BoardFactory action(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		
		BoardFactory bf = BoardFactory.getInstance();
		
		// 쿼리스트링에 no라는 파라메터가 존재하지않거나(null), 
		// no라는 파라메터의 값이 비어있다.("")
		if(req.getParameter("no") == null || req.getParameter("no").equals("")) {
			bf.setRedirect(true);
			bf.setWhereisgo("listAll.bo");
			return bf;
		} 
		
		
		int boardno = Integer.parseInt(req.getParameter("no"));
		System.out.println(boardno + "번 글을 조회하자!");

		String ipAddr = IPCheck.getIPAddr();
		BoardDAO dao = BoardDAOImpl.getInstance();
		
		// notion에 순서도 참고
		ReadCountProcess rcp = new ReadCountProcess(0, ipAddr, boardno, null);
		System.out.println(rcp.toString());
		
		try {
			if(dao.transactionProcessForReadCount(rcp) == 1) { // 트랜잭션 처리
				BoardVo board = dao.selectBoardByNo(boardno);
				
				req.setAttribute("board", board);
				
				bf.setRedirect(false);
				bf.setWhereisgo("viewBoard.jsp");
			}
		} catch (NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		System.out.println(req.getRemoteAddr());
		
		return bf;
	}

}
