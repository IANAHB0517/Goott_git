package com.miniproj.board.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.SearchControls;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.miniproj.board.controller.BoardFactory;
import com.miniproj.board.dao.BoardDAO;
import com.miniproj.board.dao.BoardDAOImpl;
import com.miniproj.error.CommonException;
import com.miniproj.etc.PagingInfo;
import com.miniproj.vodto.BoardVo;
import com.miniproj.vodto.SearchCriteria;

public class BoardListService implements BoardService {
	
	
	@Override
	public BoardFactory action(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {
		BoardFactory bf = BoardFactory.getInstance();
		
		// 검색 타입
		String searchType = "";
		if(req.getParameter("searchType") != null && !req.getParameter("searchType").equals("")) {
			searchType = req.getParameter("searchType");
		} 
		
		// 검색어
		String searchWord = "";
		if(req.getParameter("searchWord") != null && !req.getParameter("searchWord").equals("")) {
			searchWord = req.getParameter("searchWord");
		} 
		
		SearchCriteria sc = new SearchCriteria(searchType, searchWord);
		System.out.print(sc.toString());
		
		// 페이지 번호
		int pageNo = -1;
		if(req.getParameter("pageNo")==null ||req.getParameter("pageNo").equals("")) {
			pageNo = 1;
		} else {
			pageNo = Integer.parseInt(req.getParameter("pageNo"));
		}
		
		int viewPostCntPerPage = 0;
		if(req.getParameter("viewPost") == null || req.getParameter("viewPost").equals("")) {
			viewPostCntPerPage = 3;
		} else {
			viewPostCntPerPage = Integer.parseInt(req.getParameter("viewPost"));
		}
		
		// 한 페이지당 보여줄 글의 개수
		System.out.println("페이지 번호 : " + pageNo);
		
		BoardDAO dao = BoardDAOImpl.getInstance();
		
		try { 
			// 페이지번호, 전체 글의 개수로 페이징 처리를 해서
			PagingInfo pi = getPagingInfo(pageNo, viewPostCntPerPage, dao, sc);
			
			// 페이징 처리한 쿼리문이 실행되도록 dao단 호출
			List<BoardVo> lst = null;
			if(!sc.getSearchWord().equals("")) { // 검색어가 있다면
				lst = dao.selectEntireBoard(pi, sc);
			} else if(sc.getSearchWord().equals("") || sc.getSearchWord() == null) { // 검색어가 없다면
				lst= dao.selectEntireBoard(pi);
			}
			
		
			req.setAttribute("boardList", lst);
			req.setAttribute("pagingInfo", pi);
			
			bf.setRedirect(false);
			bf.setWhereisgo("listAll.jsp");
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
				bf.setWhereisgo("../index.jsp");
			}
		}
		
		
		
		return bf;
	}

	private PagingInfo getPagingInfo(int pageNo, int viewPostCntPerPage, BoardDAO dao, SearchCriteria sc) 
			throws NamingException, SQLException {
		PagingInfo pi = new PagingInfo();
		
		
		// 실질적인 페이징에 필요한 변수들 setting
		pi.setViewPostCntPerPage(viewPostCntPerPage);
		pi.setPageNo(pageNo);
		if(!sc.getSearchWord().equals("")) { // 검색어가 있다면
			pi.setTotalPostCnt(dao.getTotalPostCnt("board", sc)); // 검색어로 검색한 글의 개수
		} else if(sc.getSearchWord().equals("") || sc.getSearchWord() == null) { // 검색어가 없다면
			pi.setTotalPostCnt(dao.getTotalPostCnt("board"));
		}
		
		pi.setTotalPageCnt(pi.getTotalPostCnt(), pi.getViewPostCntPerPage());
		pi.setStartRowInd(pi.getPageNo());
		
		// 페이징 블럭 처리를 위한 필요한 변수들 setting
		
		//현재 페이지가 속한 페이징 블럭
		pi.setPageBlockOfCurrentPage(pi.getPageNo());
		
		// 현재 페이징 블럭 시작번호
		pi.setStartNumOfCurrentPagingBlock(pi.getPageBlockOfCurrentPage());
		
		// 현재 페이징 블럭 끝번호
		pi.setEndNumOfCurrentPagingBlock(pi.getStartNumOfCurrentPagingBlock());
		
		
		System.out.println(pi.toString());
		
		return pi;
	}

}
