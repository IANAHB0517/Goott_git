package com.miniproj.board.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import com.miniproj.etc.PagingInfo;
import com.miniproj.vodto.BoardVo;
import com.miniproj.vodto.ReadCountProcess;
import com.miniproj.vodto.SearchCriteria;

public interface BoardDAO {
	// 게시물 전체 리스트 얻어오는 
	List<BoardVo> selectEntireBoard() throws NamingException, SQLException;
	
	// 게시물 전체 리스트 얻어오는 + 페이징 처리
	List<BoardVo> selectEntireBoard(PagingInfo pi) throws NamingException, SQLException;
	
	// 게시물 전체 리스트 얻어오는 + 페이징 처리 + 검색어 입력
	List<BoardVo> selectEntireBoard(PagingInfo pi, SearchCriteria sc) throws NamingException, SQLException;
	
	// 다음 ref 값을 얻어오는
	int getNextRef() throws NamingException, SQLException;
	
	// 게시물 insert
	int insertBoard(BoardVo board) throws NamingException, SQLException;
	
	// 게시물 상세조회
	BoardVo selectBoardByNo(int no)  throws NamingException, SQLException;
	
	// ?번 ip주소가 ?번 글을 읽은적이 있냐? (하루 이상 1 , 하루이하 0, 읽은적없으면 -1)  
	int withinOneDayOrNot(String ipAddr, int boardNo) throws NamingException, SQLException;
	
	// 조회수 증가 하는 
	int updateReadCount(int no) throws NamingException, SQLException;
	
	// ip주소, 글번호, 읽은 시간을 insert
	int insertReadCount(String ipAddr, int boardNo) throws NamingException, SQLException;
	
	// ip주소, 글번호의 데이터의 readTime을 현재시간으로  update
	int updateReadTime(String ipAddr, int boardNo) throws NamingException, SQLException;
	
	// 조회수 증가 트랜잭션 작업
	int transactionprocessforReadCount(ReadCountProcess rcp) throws NamingException, SQLException;
	
	// 부모글이 원래글인지 답글인지 판단하여 update한다(답글이 끼어들기할 공간을 만들어준다) - 트랜잭션처리
	int updateReplyPost(BoardVo reply, Connection con) throws NamingException, SQLException;
	
	// 답글을 board테이블에 등록 - 트랜잭션 처리용
	int insertReplyPost(BoardVo reply, Connection con) throws NamingException, SQLException;
	
	// 답글 처리 트랜잭션
	int transactionProcessForReplyPost(BoardVo reply) throws NamingException, SQLException;
	
	// 전체 게시판의 글 갯수 얻어오기
	int getTotalPostCnt(String tableName) throws NamingException, SQLException;
	
	// 전체 게시판의 글 갯수 얻어오기
	int getTotalPostCnt(String tableName, SearchCriteria sc) throws NamingException, SQLException;

	
}
