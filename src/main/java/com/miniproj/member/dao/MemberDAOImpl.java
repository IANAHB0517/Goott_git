package com.miniproj.member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.miniproject.etc.PagingInfo;
import com.miniproject.vodto.LoginDTO;
import com.miniproject.vodto.MemberDTO;
import com.miniproject.vodto.MemberPointVo;

public class MemberDAOImpl implements MemberDAO {
	// 싱글톤
	private static MemberDAOImpl instance = null;
	private MemberDAOImpl() {}
	public static MemberDAOImpl getinstance() {
		if (instance == null) {
			instance = new MemberDAOImpl();
		}
		return instance;
	}
	

	@Override
	public int insertMember(MemberDTO dto) throws NamingException, SQLException {
		int result = 0;
		
		Connection con = DBConnection.dbConnect();
		if (con != null) {
			String sql = "";
			PreparedStatement pstmt = null;
			if (!dto.getUserImg().equals("")) { // 업로드 된 이미지 파일이 있음
				sql = "insert into member "
						+ "values(?, sha1(md5(?)), ?, ?, ?, ?, ?, ?, ?, ?)";
				
				pstmt = con.prepareStatement(sql);
				
				pstmt.setString(1, dto.getUserId());
				pstmt.setString(2, dto.getUserPwd());
				pstmt.setString(3, dto.getUserEmail());
				pstmt.setString(4, dto.getUserMobile());
				pstmt.setString(5, dto.getUserGender());
				pstmt.setString(6, dto.getHobbies());
				pstmt.setString(7, dto.getJob());
				pstmt.setString(8, dto.getUserImg());
				pstmt.setString(9, dto.getMemo());
				pstmt.setString(10, dto.getIsAdmin());
				
				
				
			} else { // 업로드 된 이미지 파일이 없음
				sql = "insert into member (userId, userPwd, userEmail, userMobile, userGender, hobbies, job, memo, isAdmin) "
						+ " values(?, sha1(md5(?)), ?, ?, ?, ?, ?, ?, ?)";
				
				pstmt = con.prepareStatement(sql);
				
				pstmt.setString(1, dto.getUserId());
				pstmt.setString(2, dto.getUserPwd());
				pstmt.setString(3, dto.getUserEmail());
				pstmt.setString(4, dto.getUserMobile());
				pstmt.setString(5, dto.getUserGender());
				pstmt.setString(6, dto.getHobbies());
				pstmt.setString(7, dto.getJob());
				pstmt.setString(8, dto.getMemo());
				pstmt.setString(9, dto.getIsAdmin());
				
			}
			
			// 회원가입과 포인트 부여가 트랜잭션 처리가 되어야한다. 
			con.setAutoCommit(false); // 자동으로 커밋되는걸 막아야 이후 포인트 적립까지 한꺼번에 이루어진다. (오류가 나면 회원가입을 막아야하므로)
			
			result = pstmt.executeUpdate();
			
			if (result == 1) {
				// 회원 가입한 유저에게 회원가입 포인트 점수 부여
				int result2 = addPoint(dto.getUserId(), "회원가입", con);
				
				// 포인트 부여까지 성공하면 커밋 -> 그렇지 않으면 롤백.
				if (result2 == 1) {
					con.commit();
				} else {
					con.rollback();
				}
			}
			
			con.setAutoCommit(true);
			DBConnection.dbclose(pstmt, con);
			
		}
		
		return result;
	}
	
	
	@Override
	public int selectByUserid(String userId) throws NamingException, SQLException {
		int result = 0;
		
		Connection con = DBConnection.dbConnect();
		if (con != null) {
			String query = "select count(*) as userCnt from member where userId = ?";
			
			PreparedStatement pstmt = null;
			pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, userId);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result = rs.getInt("userCnt");
			}
			
			DBConnection.dbclose(rs, pstmt, con);
		}
		
		
		return result;
	}
	@Override
	public MemberDTO loginUser(LoginDTO dto, Connection con) throws NamingException, SQLException {
		MemberDTO member = null;
		// Connection con = DBConnection.dbConnect(); < 커넥션 받아옴
		
		if (con != null) {
			String sql = "select * from member where userId = ? and userPwd = sha1(md5(?))";
			
			PreparedStatement pstmt = null;
			pstmt = con.prepareStatement(sql);
			
			pstmt.setString(1, dto.getUserId());
			pstmt.setString(2, dto.getPwd());
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				member = new MemberDTO(rs.getString("userId"),
						rs.getString("userPwd"),
						rs.getString("userEmail"),
						rs.getString("userMobile"),
						rs.getString("userGender"),
						rs.getString("hobbies"),
						rs.getString("job"),
						rs.getString("userImg"),
						rs.getString("memo"),
						rs.getString("isAdmin"));
			}
			
		} 
		
		
		return member;
	}
	
	
	// 포인트를 부여하는 메서드 
	// -> 매개를 주면 다양한 상황에서 사용할 수 있음.
	// 단, Connection 을 받아 의존도가 높음 >> spring에서는 서비스 단에서 트랜잭션 처리를 하므로 나아짐...
	@Override
	public int addPoint(String userId, String why, Connection con) throws NamingException, SQLException {
		int result = 0;
		System.out.println("포인트 부여");
//		Connection con = DBConnection.dbConnect();
		if (con != null) {
			String sql = "insert into memberpoint (who, why, howmuch) "
						+ "values (?, ?, (select howmuch from pointpolicy where why=?))";
			
				PreparedStatement pstmt = con.prepareStatement(sql);
				
				pstmt.setString(1, userId);
				pstmt.setString(2, why);
				pstmt.setString(3, why);
				
				result = pstmt.executeUpdate();
				
				System.out.println("포인트 부여 결과 : " + result);
				
//				DBConnection.dbclose(pstmt, con);
				
		}
		
		return result;
	}
	
	/**
	 *  result = -1 (null : 처음 로그인) -> 추후에 insert
	 *  result = 0 (같은 일자에 로그인 한 기록이 있는 유저) -> 추후에 update
	 *  result = 1 (최근 로그인 한 날짜가 오늘이 아닌 유저) -> 추후에 update
	 */
	
	// 가장 최근에 로그인 한 것이 오늘인지? 확인하는 메서드
	@Override
	public int whenlatestLogin(String userId, Connection con) throws NamingException, SQLException {
		int result = -1;
		
		if (con != null) {
			String sql = "select ifnull(a.diff, -1) as datediff from "
					+ "(select datediff(now(), (select latestLoginDate from latestloginlog where who=?)) as diff) a";
			
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				int tmp = rs.getInt("datediff");
				if (tmp == 0) {
					result = 0;
					
				} else if (tmp > 0) {
					result = 1 ;
				}
			}
			
		}
		System.out.println("whenlatestLogin : " + result);
		
		return result;
	}
	
	
	// 로그인 기록을 남기는 메서드
	@Override
	public int writtenLoginDate(int mode, String userId, Connection con) throws NamingException, SQLException {
		int result = 0;
		
		String sql = "";
		PreparedStatement pstmt = null;
		
		if (con != null) {
			if (mode == -1) {
				sql = "insert into latestloginlog (who) values(?)"; // 회원 가입 후 처음 로그인

			} else {
				sql = "update latestloginlog set latestLoginDate = now() where who = ?"; // 로그인 기록이 존재함

			}
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, userId);
			
			
			result = pstmt.executeUpdate();
		}
		
		return result;
	}
	
	
	// 로그인, 포인트 부여를 트랜잭션 처리
	@Override
	public MemberDTO loginWithTransaction(LoginDTO dto) throws NamingException, SQLException {
		MemberDTO loginUser = null;
		
		Connection con = DBConnection.dbConnect();
		
		con.setAutoCommit(false); // 트랜잭션 시작
		
		if (con != null) {
			MemberDTO loginmember = loginUser(dto, con);
			int addpointReseult = -1 ;
			// 로그인이 성공했다는 전제하에 모든 프로세스를 처리함.
			if (loginmember != null) { // 로그인을 성공했다면
				System.out.println("로그인 한 유저 : " + loginmember.toString());
				int loginDate = whenlatestLogin(loginmember.getUserId(), con);
				System.out.println("로그인 상태 : " + loginDate);
				if (loginDate > 0 || loginDate == -1) {
					// 포인트를 부여함
					addpointReseult = addPoint(loginmember.getUserId(), "로그인", con);
					System.out.println("포인트 부여 : " + addpointReseult);
				}
				// 로그인 기록을 남기는 메서드를 호출하여 insert, update 작업을 수행하게 함
				int writtenLoginDateReseult = writtenLoginDate(loginDate, loginmember.getUserId(), con);
				System.out.println("로그인 기록 : " + addpointReseult);
				
				// 로그인이 성공하고, 포인트 값도 남겨줘야 진짜 로그인 성공임.
				if (addpointReseult == 1 && writtenLoginDateReseult == 1) {
					loginUser = loginmember;
					con.commit(); 
					
				} else if (addpointReseult == -1 && writtenLoginDateReseult == 1) {
					loginUser = loginmember;
					con.commit(); 
					
				} else {
					con.rollback(); // 실패하면 롤백
				}
			}
			
			// 트랜잭션 처리 끝
			con.setAutoCommit(true);
			con.close();
			
		}
		
		
		return loginUser;
	}
	
	@Override
	public MemberDTO getMemberInfo(String userId) throws NamingException, SQLException {
		MemberDTO memberInfo = null;
		
		Connection con = DBConnection.dbConnect();
		if (con != null) {
			String query = "select * from member where userId = ?";
			
			PreparedStatement pstmt = con.prepareStatement(query);
			pstmt.setString(1, userId);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				memberInfo = new MemberDTO(rs.getString("userId"),
									rs.getString("userPwd"),
									rs.getString("userEmail"),
									rs.getString("userMobile"),
									rs.getString("userGender"),
									rs.getString("hobbies"),
									rs.getString("job"),
									rs.getString("userImg"),
									rs.getString("memo"),
									rs.getString("isAdmin"));
			}
			
			DBConnection.dbclose(rs, pstmt, con);
		}
		
		
		return memberInfo;
	}
	
	// DB에서 포인트 가져오는 문
	@Override
	public List<MemberPointVo> getMemberPoint(String userId, PagingInfo pi) throws NamingException, SQLException {
		List<MemberPointVo> lst = new ArrayList<>();
		
		Connection con = DBConnection.dbConnect();
		if (con != null) {
			String query = "select * from memberpoint where who = ? order by no desc limit ?, ?";
			
			PreparedStatement pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, userId);
			pstmt.setInt(2, pi.getStartRowIndex());
			pstmt.setInt(3, pi.getViewPostCntPerPage());
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				lst.add(new MemberPointVo(
						rs.getInt("no"),
						rs.getString("who"),
						rs.getTimestamp("when"),
						rs.getString("why"),
						rs.getInt("howmuch")));
				
			}
			DBConnection.dbclose(rs, pstmt, con);
			
		}
		
		return lst;
	}
	
	
	// 멤버 포인트 가져오는? 메서드? 
	@Override
	public int getTotalPointCnt(String userId) throws NamingException, SQLException {
		int cnt = -1;
		
		Connection con = DBConnection.dbConnect();
		if (con != null) {
			String query = "select count(*) as cnt from memberpoint where who = ?";
			
			PreparedStatement pstmt = con.prepareStatement(query);
			
			pstmt.setString(1, userId);
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				cnt = rs.getInt("cnt");
			}
			DBConnection.dbclose(rs, pstmt, con);
			
		}
		
		return cnt;
	}

}
