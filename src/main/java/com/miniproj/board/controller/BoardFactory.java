package com.miniproj.board.controller;

import com.miniproj.board.service.BoardListService;
import com.miniproj.board.service.BoardReplyService;
import com.miniproj.board.service.BoardService;
import com.miniproj.board.service.BoardViewService;
import com.miniproj.board.service.BoardWriteServce;

public class BoardFactory {
	private static BoardFactory instance = null;
	private boolean isRedirect;  // 리다이렉인지 아닌지
	private String whereisgo;  // 어느페이지로 갈지
	
	private BoardFactory() { }
	
	public static BoardFactory getInstance() {
		if (instance == null) {
			instance = new BoardFactory();
		}
		
		return instance;
	}
	
	
	public boolean isRedirect() {
		return isRedirect;
	}

	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}

	public String getWhereisgo() {
		return whereisgo;
	}

	public void setWhereisgo(String whereisgo) {
		this.whereisgo = whereisgo;
	}

	public BoardService getService(String command) {
		BoardService service = null;
		
		if (command.equals("/board/listAll.bo"))  {
			service = new BoardListService();
		} else if (command.equals("/board/write.bo")) {
			service = new BoardWriteServce();
		} else if (command.equals("/board/viewBoard.bo")) {
			service = new BoardViewService();
		} else if (command.equals("/board/reply.bo")) {
			service = new BoardReplyService();
		}
		
		return service;
		
	}

	@Override
	public String toString() {
		return "BoardFactory [isRedirect=" + isRedirect + ", whereisgo=" + whereisgo + "]";
	}
	
	
}
