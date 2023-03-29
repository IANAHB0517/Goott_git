package com.miniproj.board.service;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.List;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.miniproj.board.controller.BoardFactory;
import com.miniproj.board.dao.BoardDAO;
import com.miniproj.board.dao.BoardDAOImpl;
import com.miniproj.etc.FileNameProcessing;
import com.miniproj.vodto.BoardVo;
import com.miniproj.vodto.MemberDTO;

public class BoardWriteServce implements BoardService {

	// 파일 업로드를 위한 세팅
	// (하나의 파일 블럭이 들어오는 버퍼 사이즈)5MB
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 5;
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 10; // 최대 파일 업로드 크기(10MB)
	// 최대 request 버퍼 크기(15MB)
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 15;

	@Override
	public BoardFactory action(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		BoardFactory bf = BoardFactory.getInstance();

		try {
			BoardVo board = getData(req); // 넘겨져온 텍스트 데이터와 파일을 분리하여 저장
			
			BoardDAO dao = BoardDAOImpl.getInstance();
			
			if (dao.insertBoard(board) == 1) {
				bf.setRedirect(true);
				bf.setWhereisgo("listAll.bo"); // 글 전체를 가져온 후 다시 글전체 페이지로
			} else {
				bf.setRedirect(true);
				bf.setWhereisgo("listAll.bo?status=fail");
			}
			
		} catch (FileUploadException | NamingException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		

		return bf;
	}

	private BoardVo getData(HttpServletRequest req)
			throws FileUploadException, UnsupportedEncodingException, NamingException, SQLException {
		String upload = "\\board\\imgs";
		ServletContext context = req.getServletContext(); // 현재 request 대응하는 서블릿 객체를 얻음

		String realPath = context.getRealPath(upload); // 파일이 업로드될 물리적 경로
		System.out.println("파일이 서버에 저장될 실제 경로 : " + realPath);

		String encoding = "utf-8"; // 텍스트 데이터, 파일 이름 인코딩을 위해

		// 파일을 저장하기 위한 File 객체 생성
		File saveFileDir = new File(realPath);

		String writer = "";
		String title = "";
		String content = "";
		String imgFile = "";
		
		// 파일이 저장될 공간의 경로, 사이즈 등의 정보를 가지고 있는 객체
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(saveFileDir);
		factory.setSizeThreshold(MEMORY_THRESHOLD);

		// 실제 request로 넘겨져온 매개변수를 통해 파일을 upload 할 객체
		ServletFileUpload sfu = new ServletFileUpload(factory);
		sfu.setFileSizeMax(MAX_FILE_SIZE);
		sfu.setSizeMax(MAX_REQUEST_SIZE);
		
		List<FileItem> lst = sfu.parseRequest(req);
		for (FileItem fi : lst) {
			if(fi.isFormField()) {
				if(fi.getFieldName().equals("writer")) {
					// request에서 얻어옴
//					writer = fi.getString(encoding);
					
					// 세션에서 얻어옴
					HttpSession ses = req.getSession();
					MemberDTO loginMember = (MemberDTO)ses.getAttribute("loginMember");
					writer = loginMember.getUserId();
					
				} else if (fi.getFieldName().equals("title")) {
					title = fi.getString(encoding);
				} else if (fi.getFieldName().equals("content")) {
					content = fi.getString(encoding);
				}
			} else { // 파일이 있다면...
				imgFile = FileNameProcessing.getNewFileName(fi, realPath); // 중복되지 않는 새로운파일이름얻기
				
				File uploadFilePath = new File(realPath + File.separator + imgFile);
				
				try {
					fi.write(uploadFilePath); // 실제 저장
				} catch (Exception e) {
					// 유저가 업로드한 파일이 저장이 안되었다. -> 글 등록은 되어야 함.
					imgFile = "";
				}
			}
		}
		
		String dbUserImg = "";
		// DB에 insert 하기 전 업로드된 파일이 있는지?
		if (!imgFile.equals("")) { // 업로드된 이미지가 존재 한다면
			dbUserImg = "board/imgs/" + imgFile; // DB에 경로까지 포함해서 insert 한다.
		}
		
		int ref = BoardDAOImpl.getInstance().getNextRef();
		
		
		content = content.replace("\n", "<br>"); // 줄바꿈
		
		// DAO으로 전송하기 위해
		BoardVo board = new BoardVo(0, writer, title, null, content, dbUserImg, 0, 0, ref, 0, 0);
		
		System.out.println(board.toString());
		
		return board;
		
	}

}
