package com.miniproj.member.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;

import com.miniproj.etc.PagingInfo;
import com.miniproj.vodto.LoginDTO;
import com.miniproj.vodto.MemberDTO;
import com.miniproj.vodto.MemberPointVo;

public interface MemberDAO {
	// 회원 가입 
	int insertMember(MemberDTO dto) throws NamingException, SQLException; 
	
	// 아이디 중복검사
	int selectByUserId(String userId) throws NamingException, SQLException;
	
	// 로그인
	MemberDTO loginUser(LoginDTO dto, Connection con) throws NamingException, SQLException;
	
	// 포인트 점수 부여
	int addPoint(String userId, String why, Connection con) throws NamingException, SQLException;
	
	// 가장 최근에 로그인 한 것이 오늘인지? 
	int whenLatestLogin(String userId, Connection con)  throws NamingException, SQLException;
	
	// 로그인한 유저의 로그인 기록 (insert or update)
	int writtenLoginDate(int mode, String userId, Connection con)  throws NamingException, SQLException;
	
	// 로그인, 포인트 부여를 트랜잭션 처리
	MemberDTO loginWithTransaction(LoginDTO dto) throws NamingException, SQLException;
	
	// 유저아이디로 회원 정보 불러오기
	MemberDTO getMemberInfo(String userId) throws NamingException, SQLException;
	
	// 포인트 내역 가져오기
	List<MemberPointVo> getMemberPoint(String userId, PagingInfo pi) throws NamingException, SQLException;

	// 유저의 멤버 포인트 갯수 얻어오기
	int getTotalPointCnt(String userId) throws NamingException, SQLException;
}
