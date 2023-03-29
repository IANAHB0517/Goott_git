package com.miniproj.board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;

import com.miniproj.etc.PagingInfo;
import com.miniproj.member.dao.DBConnection;
import com.miniproj.member.dao.MemberDAOImpl;
import com.miniproj.vodto.BoardVo;
import com.miniproj.vodto.ReadCountProcess;
import com.miniproj.vodto.SearchCriteria;

public class BoardDAOImpl implements BoardDAO {
	private static BoardDAOImpl instance = null;

	private BoardDAOImpl() {
	}

	public static BoardDAOImpl getInstance() {
		if (instance == null) {
			instance = new BoardDAOImpl();
		}

		return instance;
	}

	@Override
	public List<BoardVo> selectEntireBoard() throws NamingException, SQLException {
		List<BoardVo> lst = new ArrayList<>();

		Connection con = DBConnection.dbConnect();

		if (con != null) {
			String q = "select * from board order by ref desc, reforder asc";

			PreparedStatement pstmt = con.prepareStatement(q);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				lst.add(new BoardVo(rs.getInt("no"), rs.getString("writer"), rs.getString("title"),
						rs.getTimestamp("postDate"), rs.getString("content"), rs.getString("imgFile"),
						rs.getInt("readcount"), rs.getInt("likecount"), rs.getInt("ref"), rs.getInt("step"),
						rs.getInt("reforder")));
			}
			DBConnection.dbClose(rs, pstmt, con);
		}

		return lst;
	}

	@Override
	public List<BoardVo> selectEntireBoard(PagingInfo pi) throws NamingException, SQLException {
		List<BoardVo> lst = new ArrayList<>();

		Connection con = DBConnection.dbConnect();

		if (con != null) {
			String q = "select * from board order by ref desc, reforder asc limit ?, ?";

			PreparedStatement pstmt = con.prepareStatement(q);
			pstmt.setInt(1, pi.getStartRowInd());
			pstmt.setInt(2, pi.getViewPostCntPerPage());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				lst.add(new BoardVo(rs.getInt("no"), rs.getString("writer"), rs.getString("title"),
						rs.getTimestamp("postDate"), rs.getString("content"), rs.getString("imgFile"),
						rs.getInt("readcount"), rs.getInt("likecount"), rs.getInt("ref"), rs.getInt("step"),
						rs.getInt("reforder")));
			}

			DBConnection.dbClose(rs, pstmt, con);
		}

		return lst;
	}

	@Override
	public List<BoardVo> selectEntireBoard(PagingInfo pi, SearchCriteria sc) throws NamingException, SQLException {
		List<BoardVo> lst = new ArrayList<>();

		Connection con = DBConnection.dbConnect();

		if (con != null) {
			String q = "select * from board where ";

			if (sc.getSearchType().equals("title")) {
				q += "title like ? order by ref desc, reforder asc limit ?, ?";

			} else if (sc.getSearchType().equals("writer")) {
				q += "writer like ? order by ref desc, reforder asc limit ?, ?";
			} else if (sc.getSearchType().equals("content")) {
				q += "content like ? order by ref desc, reforder asc limit ?, ?";
			}

			PreparedStatement pstmt = con.prepareStatement(q);
			pstmt.setString(1, "%" + sc.getSearchWord() + "%");
			pstmt.setInt(2, pi.getStartRowInd());
			pstmt.setInt(3, pi.getViewPostCntPerPage());

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				lst.add(new BoardVo(rs.getInt("no"), rs.getString("writer"), rs.getString("title"),
						rs.getTimestamp("postDate"), rs.getString("content"), rs.getString("imgFile"),
						rs.getInt("readcount"), rs.getInt("likecount"), rs.getInt("ref"), rs.getInt("step"),
						rs.getInt("reforder")));
			}

			DBConnection.dbClose(rs, pstmt, con);
		}

		return lst;
	}

	@Override
	public int getNextRef() throws NamingException, SQLException {
		int nextRef = 0;

		Connection con = DBConnection.dbConnect();

		if (con != null) {
			String q = "select max(no) + 1 as nextRef from board";

			PreparedStatement pstmt = con.prepareStatement(q);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				nextRef = rs.getInt("nextRef");
			}

			DBConnection.dbClose(rs, pstmt, con);
		}

		return nextRef;
	}

	@Override
	public int insertBoard(BoardVo board) throws NamingException, SQLException {
		// 글 등록 -> addPoint (트랜잭션 처리)
		int result = 0;

		Connection con = DBConnection.dbConnect();

		con.setAutoCommit(false); // 트랜잭션 시작

		if (con != null) {
			String q = "insert into board(writer, title, content, imgFile, ref) " + "values(?, ?, ?, ?, ?)";

			PreparedStatement pstmt = con.prepareStatement(q);

			pstmt.setString(1, board.getWriter());
			pstmt.setString(2, board.getTitle());
			pstmt.setString(3, board.getContent());
			pstmt.setString(4, board.getImgFile());
			pstmt.setInt(5, board.getRef());

			// 게시물 등록
			int writeResult = pstmt.executeUpdate();

			// 글쓴이에게 포인트 부여
			int pointResult = MemberDAOImpl.getInance().addPoint(board.getWriter(), "게시판글쓰기", con);

			if (writeResult == 1 && pointResult == 1) {
				con.commit();
				result = 1;
			} else {
				con.rollback();
			}

			con.setAutoCommit(true);

			DBConnection.dbClose(pstmt, con);

		}

		return result;
	}

	@Override
	public BoardVo selectBoardByNo(int no) throws NamingException, SQLException {
		BoardVo board = null;

		Connection con = DBConnection.dbConnect();

		if (con != null) {
			String q = "select * from board where no=?";

			PreparedStatement pstmt = con.prepareStatement(q);
			pstmt.setInt(1, no);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				board = new BoardVo(rs.getInt("no"), rs.getString("writer"), rs.getString("title"),
						rs.getTimestamp("postDate"), rs.getString("content"), rs.getString("imgFile"),
						rs.getInt("readcount"), rs.getInt("likecount"), rs.getInt("ref"), rs.getInt("step"),
						rs.getInt("reforder"));
			}
			DBConnection.dbClose(rs, pstmt, con);
		}

		return board;
	}

	/**
	 * ?번 ip주소가 ?번 글을 읽은적이 있냐? return 값 : (하루 이상 1 , 하루이하 0, 읽은적없으면 -1)
	 */
	@Override
	public int withinOneDayOrNot(String ipAddr, int boardNo) throws NamingException, SQLException {
		int result = -1;

		Connection con = DBConnection.dbConnect();

		if (con != null) {
			String q = "select ifNull(timestampdiff(hour, " + "(select readtime from readcountprocess where ipAddr = ? "
					+ "and boardNo = ?), now()) > 24, -1) as diff";

			PreparedStatement pstmt = con.prepareStatement(q);
			pstmt.setString(1, ipAddr);
			pstmt.setInt(2, boardNo);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				result = rs.getInt("diff");
			}

			DBConnection.dbClose(rs, pstmt, con);
		}

		return result;
	}

	// 트랜잭션 처리용 int withinOneDayOrNot(String ipAddr, int boardNo) 오버로딩
	/*
	 * 
	 *
	 * ?번 ip주소가 ?번 글을 읽은적이 있냐? return 값 : (하루 이상 1 , 하루이하 0, 읽은적없으면 -1)
	 */
	public int withinOneDayOrNot(String ipAddr, int boardNo, Connection con) throws NamingException, SQLException {
		int result = -1;

		// Connection con = DBConnection.dbConnect();

		if (con != null) {
			String q = "select ifNull(timestampdiff(hour, " + "(select readtime from readcountprocess where ipAddr = ? "
					+ "and boardNo = ?), now()) > 24, -1) as diff";

			PreparedStatement pstmt = con.prepareStatement(q);
			pstmt.setString(1, ipAddr);
			pstmt.setInt(2, boardNo);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				result = rs.getInt("diff");
			}

			// DBConnection.dbClose(rs, pstmt, con);
		}

		return result;
	}

	@Override
	public int updateReadCount(int no) throws NamingException, SQLException {
		int result = 0;

		Connection con = DBConnection.dbConnect();

		if (con != null) {
			String q = "update board set readcount = readcount + 1 where no = ?";

			PreparedStatement pstmt = con.prepareStatement(q);

			pstmt.setInt(1, no);

			result = pstmt.executeUpdate();

			DBConnection.dbClose(pstmt, con);

		}

		return result;
	}

	public int updateReadCount(int no, Connection con) throws NamingException, SQLException {
		int result = 0;

//		Connection con = DBConnection.dbConnect();

		if (con != null) {
			String q = "update board set readcount = readcount + 1 where no = ?";

			PreparedStatement pstmt = con.prepareStatement(q);

			pstmt.setInt(1, no);

			result = pstmt.executeUpdate();

//			DBConnection.dbClose(pstmt, con);

		}

		return result;
	}

	@Override
	public int insertReadCount(String ipAddr, int boardNo) throws NamingException, SQLException {
		int result = 0;

		Connection con = DBConnection.dbConnect();

		if (con != null) {
			String q = "insert into readcountprocess(ipAddr, boardNo) " + "values (?, ?)";

			PreparedStatement pstmt = con.prepareStatement(q);

			pstmt.setString(1, ipAddr);
			pstmt.setInt(2, boardNo);

			result = pstmt.executeUpdate();

			DBConnection.dbClose(pstmt, con);

		}

		return result;
	}

	@Override
	public int updateReadTime(String ipAddr, int boardNo) throws NamingException, SQLException {
		int result = 0;

		Connection con = DBConnection.dbConnect();

		if (con != null) {
			String q = "update readcountprocess set readTime = now() " + "where ipAddr=? and boardNo=?";

			PreparedStatement pstmt = con.prepareStatement(q);

			pstmt.setString(1, ipAddr);
			pstmt.setInt(2, boardNo);

			result = pstmt.executeUpdate();

			DBConnection.dbClose(pstmt, con);

		}

		return result;
	}

	public int updateReadTime(String ipAddr, int boardNo, Connection con) throws NamingException, SQLException {
		int result = 0;

//		Connection con = DBConnection.dbConnect();

		if (con != null) {
			String q = "update readcountprocess set readTime = now() " + "where ipAddr=? and boardNo=?";

			PreparedStatement pstmt = con.prepareStatement(q);

			pstmt.setString(1, ipAddr);
			pstmt.setInt(2, boardNo);

			result = pstmt.executeUpdate();

//			DBConnection.dbClose(pstmt, con);

		}

		return result;
	}

	@Override
	public int transactionprocessforReadCount(ReadCountProcess rcp) throws NamingException, SQLException {
		int result = -1;

		Connection con = DBConnection.dbConnect();

		con.setAutoCommit(false); // 트랜잭션 시작 지점

		if (con != null) {
			int oneday = withinOneDayOrNot(rcp.getIpAddr(), rcp.getBoardNo(), con);
			if (oneday == -1) {
				int insertResult = insertReadCount(rcp.getIpAddr(), rcp.getBoardNo(), con);
				int updateResult = updateReadCount(rcp.getBoardNo(), con);

				if (insertResult == 1 && updateResult == 1) {
					con.commit();
					result = 1;
				} else {
					con.rollback();
				}

			} else if (oneday == 1) {
				int updateResult1 = updateReadTime(rcp.getIpAddr(), rcp.getBoardNo(), con);
				int updateResult2 = updateReadCount(rcp.getBoardNo(), con);

				if (updateResult1 == 1 && updateResult2 == 1) {
					con.commit();
					result = 1;
				} else {
					con.rollback();
				}
			} else {
				result = 1;
			}
		}

		con.setAutoCommit(true); // 트랜잭션 종료 지점

		DBConnection.dbClose(con);
		return result;
	}

	private int insertReadCount(String ipAddr, int boardNo, Connection con) throws NamingException, SQLException {
		int result = 0;

//		Connection con = DBConnection.dbConnect();

		if (con != null) {
			String q = "insert into readcountprocess(ipAddr, boardNo) " + "values (?, ?)";

			PreparedStatement pstmt = con.prepareStatement(q);

			pstmt.setString(1, ipAddr);
			pstmt.setInt(2, boardNo);

			result = pstmt.executeUpdate();

//			DBConnection.dbClose(pstmt, con);

		}

		return result;
	}

	@Override
	public int updateReplyPost(BoardVo reply, Connection con) throws NamingException, SQLException {
		int result = -1;

		if (con != null) {
			String q = "update board " + "set reforder = reforder + 1 " + "where ref=? and reforder > ?";

			PreparedStatement pstmt = con.prepareStatement(q);

			pstmt.setInt(1, reply.getRef());
			pstmt.setInt(2, reply.getReforder());

			result = pstmt.executeUpdate();

		}

		return result;
	}

	@Override
	public int insertReplyPost(BoardVo reply, Connection con) throws NamingException, SQLException {
		int result = 0;

		if (con != null) {
			String q = "insert into board(writer, title, content, ref, step, reforder) " + "values (?, ?, ?, ?, ?, ?)";

			// ?, ?, ?, ?, pStep+1, pRefOrder+1
			PreparedStatement pstmt = con.prepareStatement(q);

			pstmt.setString(1, reply.getWriter());
			pstmt.setString(2, reply.getTitle());
			pstmt.setString(3, reply.getContent());

			pstmt.setInt(4, reply.getRef());
			pstmt.setInt(5, reply.getStep() + 1);
			pstmt.setInt(6, reply.getReforder() + 1);

			result = pstmt.executeUpdate();

		}

		return result;
	}

	@Override
	public int transactionProcessForReplyPost(BoardVo reply) throws NamingException, SQLException {
		int result = -1;

		Connection con = DBConnection.dbConnect();
		con.setAutoCommit(false);
		if (con != null) {
			int updateResult = updateReplyPost(reply, con);
			System.out.println("updateResult : " + updateResult);
			if (updateResult >= 0) {
				int insertResult = insertReplyPost(reply, con);
				System.out.println("insertResult : " + insertResult);
				if (insertResult == 1) {

					int addPoint = MemberDAOImpl.getInance().addPoint(reply.getWriter(), "게시판답글쓰기", con);

					if (addPoint == 1) {
						con.commit();
						result = 1;
					} else {
						con.rollback();
					}
				}
			}
		}

		con.setAutoCommit(true);
		DBConnection.dbClose(con);
		return result;
	}

	@Override
	public int getTotalPostCnt(String tableName) throws NamingException, SQLException {
		int result = -1;

		Connection con = DBConnection.dbConnect();

		if (con != null) {
			String q = "select count(*) as cnt from " + tableName;

			PreparedStatement pstmt = con.prepareStatement(q);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				result = rs.getInt("cnt");
			}

			DBConnection.dbClose(rs, pstmt, con);
		}

		return result;
	}

	@Override
	public int getTotalPostCnt(String tableName, SearchCriteria sc) throws NamingException, SQLException {
		int result = -1;

		Connection con = DBConnection.dbConnect();

		if (con != null) {
			String q = "select count(*) as cnt from " + tableName + " where ";

			if (sc.getSearchType().equals("title")) {
				q += "title like ?";

			} else if (sc.getSearchType().equals("writer")) {
				q += "writer like ?";
			} else if (sc.getSearchType().equals("content")) {
				q += "content like ?";
			}

			PreparedStatement pstmt = con.prepareStatement(q);
			pstmt.setString(1, "%" + sc.getSearchWord() + "%");
			

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				result = rs.getInt("cnt");
			}

			DBConnection.dbClose(rs, pstmt, con);
		}

		return result;
	}

}
