package com.miniproj.member.service;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import com.miniproj.error.CommonException;
import com.miniproj.member.controller.MemberFactory;
import com.miniproj.member.dao.MemberDAO;
import com.miniproj.member.dao.MemberDAOImpl;
import com.miniproj.vodto.MemberDTO;

public class RegisterMemberService implements MemberService {

	// 파일 업로드를 위한 세팅
	// (하나의 파일 블럭이 들어오는 버퍼 사이즈)5MB
	private static final int MEMORY_THRESHOLD = 1024 * 1024 * 5;
	private static final int MAX_FILE_SIZE = 1024 * 1024 * 10; // 최대 파일 업로드 크기(10MB)
	// 최대 request 버퍼 크기(15MB)
	private static final int MAX_REQUEST_SIZE = 1024 * 1024 * 15;

	@Override
	public MemberFactory execute(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		MemberFactory mf = MemberFactory.getInstance();

		String upload = "\\uploadMember";
		ServletContext context = req.getServletContext(); // 현재 request 대응하는 서블릿 객체를 얻음

		String realPath = context.getRealPath(upload); // 파일이 업로드될 물리적 경로
		System.out.println("파일이 서버에 저장될 실제 경로 : " + realPath);

		String encoding = "utf-8"; // 텍스트 데이터, 파일 이름 인코딩을 위해

		// 파일을 저장하기 위한 File 객체 생성
		File saveFileDir = new File(realPath);

		String userId = "";
		String userPwd = "";
		
		String userEmail = "";
		String userMobile = "";
		String userGender = "";
		String hobbies = "";
		List<String> hobbyLst = new ArrayList<>();
		String job = "";
		String userImg = "";
		String memo = "";

		// 파일이 저장될 공간의 경로, 사이즈 등의 정보를 가지고 있는 객체
		DiskFileItemFactory factory = new DiskFileItemFactory();
		factory.setRepository(saveFileDir);
		factory.setSizeThreshold(MEMORY_THRESHOLD);

		// 실제 request로 넘겨져온 매개변수를 통해 파일을 upload 할 객체
		ServletFileUpload sfu = new ServletFileUpload(factory);
		sfu.setFileSizeMax(MAX_FILE_SIZE);
		sfu.setSizeMax(MAX_REQUEST_SIZE);

		// 중요!!! - 파일 업로드시 함께 넘겨져온 다른 텍스트 데이터를 아래와 같이
		// request.getParameter(); 를 사용할 수 없다.
//		userId = req.getParameter("userId");
//		System.out.println(userId);

		try {
//			FileItem의 특징
//			1) name 속성의 값이 이미지는 null이 아니고 파일 이름이다. 
			// (이미지가 아닌 다른 데이터는 name속성의 값이 null이다)
//			2) 파일의 isFormField 속성의 값은 false (이진 파일이지 폼 데이터가 아님), 
			// 이미지 파일이 아닌데이터는 isFormField의 속성이 true
//			3) FieldName 속성의 값은 데이터가 넘겨져온 매개변수 이름이다.

			List<FileItem> lst = sfu.parseRequest(req);
			for (FileItem fi : lst) {
//				System.out.println(fi);

				if (fi.isFormField()) { // 이미지가 아니다
					if (fi.getFieldName().equals("userId")) {
						userId = fi.getString(encoding);
					} else if (fi.getFieldName().equals("pwd")) {
						userPwd = fi.getString(encoding);
					} else if (fi.getFieldName().equals("email")) {
						userEmail = fi.getString(encoding);
					} else if (fi.getFieldName().equals("mobile")) {
						userMobile = fi.getString(encoding);
					} else if (fi.getFieldName().equals("gender")) {
						userGender = fi.getString(encoding);
					} else if (fi.getFieldName().equals("job")) {
						job = fi.getString(encoding);
					} else if (fi.getFieldName().equals("memo")) {
						memo = fi.getString(encoding);
					} else if (fi.getFieldName().equals("hobby")) {
						hobbyLst.add(fi.getString(encoding));
					}

				} else { // isFormField() : false -> 이미지 파일이다
					userImg = getNewFileName(fi, userId, realPath);
					// 업로드된 파일을 실제 저장
					// 1) File객체 생성
					// 디렉토리(폴더) 구분자
					// 영문 windows : \
					// 한글 windows : \(원표시)
					// linux 계열 : /
					// -> File.separator : 운영체제 마다 다른 디렉토리 구분자를 정의한 상수

					File uploadFilePath = new File(realPath + File.separator + userImg);
					// 2) 실제 저장
					try {
						fi.write(uploadFilePath);
					} catch (Exception e) {
						// 유저가 업로드한 파일이 저장이 안되었다. -> 회원 가입은 됨
						userImg = "";
					}
				}

			}

		} catch (FileUploadException e) {
			// request객체에 대해 파싱 에러일 수도 있으므로 이때는 회원 가입이 안되어야 함.
			mf.setRedirect(true);
			mf.setWhereisgo("register.jsp?status=fail");
			return mf;
		}

		// 취미 여러개를 한개의 컬럼(hobbies)에 넣기 위해 콤마로 묶음
		for (int i = 0; i < hobbyLst.size(); i++) {
			if (i != hobbyLst.size() - 1) {
				hobbies += hobbyLst.get(i) + ", ";
			} else {
				hobbies += hobbyLst.get(i);
			}
		}

		String dbUserImg = "";
		// DB에 insert 하기 전 업로드된 파일이 있는지?
		if (!userImg.equals("")) { // 업로드된 이미지가 존재 한다면
			dbUserImg = "uploadMember/" + userImg; // DB에 경로까지 포함해서 insert 한다.
		}

		// 만약 base64문자열로 파일을 DB에 넣고 싶다면...
		if (userImg != "") {
			String strUpFilePath = realPath + File.separator + userImg;
//			makeFileToBase64String(strUpFilePath, realPath, userImg);
		}
		
		

		// DAO으로 전송하기 위해
		MemberDTO member = new MemberDTO(userId, userPwd, userEmail, userMobile, userGender, hobbies, job,
				dbUserImg, memo, null);

		System.out.println(member.toString());

		MemberDAO dao = MemberDAOImpl.getInance();
		try {
			if (dao.insertMember(member) == 1) {
				// 회원가입 잘됨
				// memberpoint 테이블에 회원가입 점수 부여 insert해야함.

				// 하지만 트랙잭션 처리를 위해(Connection 객체가 서비스단에는 없기 때문에)
				// -> DAO(insertMember)에서 호출
				
				
				mf.setRedirect(true); // 리다이렉트 하겠다
				mf.setWhereisgo("../index.jsp?status=success"); // 이 페이지로 가겠다
			}
		} catch (NamingException | SQLException e) {

			// DB에 insert되지 않았으므로 회원 가입이 안됨 -> 유저가 업로드한 파일을 삭제
			File uploadFilePath = new File(realPath + File.separator + userImg);
			System.out.println(realPath + File.separator + userImg + "를 삭제 합니다.");
			uploadFilePath.delete(); // 유저가 업로드한 파일 삭제

			if (e instanceof NamingException) {
				// NamingException은 개발자 실수이기 때문에 개발자만 보도록 공통 에러페이지(error.jsp)를 만들었고
				// 에러 정보를 error.jsp로 바인딩하여 error.jsp페이지에서 에러 정보를 출력하였다.
				// forward
				
				CommonException ce = new CommonException(e.getMessage(), 99);
//				throw ce; // 강제로 예외를 발생 시킴
				
				ce.setErrorMsg(e.getMessage());
				ce.setStackTrace(e.getStackTrace());
				
				req.setAttribute("error", ce);  // 에러 정보를 가진 CommonException 객체 바인딩
				
				req.getRequestDispatcher("../error.jsp").forward(req, resp);  // 페이지 이동
				
			} else if (e instanceof SQLException) {
				// SQL Exception 은 대부분 실제 유저의 입력 오류로 인한 예외
				e.printStackTrace();
				
				mf.setRedirect(true);
				mf.setWhereisgo("register.jsp?status=fail");
				return mf;
			}

		}

		return mf;
	}

	
	private String makeFileToBase64String(String strUpFilePath, String realPath, String userImg) {
		// base64 문자열 : 이진데이터 파일을 읽어서 A-Za-z0-9+/ 문자의 조합으로 바꾼것
		// 파일 -> 문자열로표현
		
		String result = null;
		File upFile = new File(strUpFilePath);
		try {
			byte[] file = FileUtils.readFileToByteArray(upFile); // 업로드 된 파일을 읽음
			result = Base64.getEncoder().encodeToString(file);  // 읽은 파일을 base64로 인코딩
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(result); // 인코딩된 문자열
		
		System.out.println(result.length()); // 인코딩된 문자열
		
		// 디코딩 
		byte[] decodeFile = Base64.getDecoder().decode(result);
		userImg.substring(userImg.lastIndexOf(".") + 1);
		try {
			FileUtils.writeByteArrayToFile(new File(realPath + File.separator + "decode." 
					+ userImg.substring(userImg.lastIndexOf(".") + 1) ), decodeFile); // 파일로 저장
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
		
		
	}

	// 업로드 된 파일의 이름을 중복되지 않는 이름을 반환
	private String getNewFileName(FileItem fi, String userId, String realPath) {
		long tmpFileSize = fi.getSize(); // 파일 사이즈
		String tmpFileName = fi.getName(); // 유저가 업로드한 파일의 이름(확장자 포함)
		String newFileName = ""; // 실제 저장되는 파일명
		if (tmpFileSize > 0) {
			// 파일 이름 처리를 어떻게 할 것이냐(같은 이름의 파일이 있으면 overwrite 되기 때문에..)
			// 1) 아예 처음 부터 중복되지 않을 이름으로 저장 하는 방법
			// ex) "userID_유니크값.확장자"

//			newFileName = makeNewUniqueFileName(userId, tmpFileName);

			// 2) 파일을 저장하기 전에 같은 이름의 파일이 존재하는지 검사하여
			// 같은 이름의 파일이 존재한다면
			// ex) "파일명(번호).확장자" 로 처리 하는 방법

			int cnt = 0;
			while (duplicateFileName(tmpFileName, realPath)) { // 파일이 중복되면
				cnt++;
				tmpFileName = makeNewFileNameWithNumbering(tmpFileName, cnt);
			}

			newFileName = tmpFileName;

			System.out.println(newFileName);

		}

		return newFileName;

	}

	private String makeNewFileNameWithNumbering(String tmpFileName, int cnt) {
		// ex) "파일명(번호).확장자"
		String newFileName = "";
		String ext = tmpFileName.substring(tmpFileName.lastIndexOf(".") + 1);
		String oldFileNameWithoutExt = tmpFileName.substring(0, tmpFileName.lastIndexOf("."));

		int openPos = oldFileNameWithoutExt.indexOf("(");
		if (openPos == -1) { // ( 가 없다면 -> 처음 중복
			newFileName = oldFileNameWithoutExt + "(" + cnt + ")." + ext;
		} else { // 김태희(1).jpg 가 있다.
			newFileName = oldFileNameWithoutExt.substring(0, openPos) + "(" + cnt + ")." + ext;
		}

		return newFileName;
	}

	// tmpFileName의 파일이 realPath 존재한다면 true, 아니면 false반환
	private boolean duplicateFileName(String tmpFileName, String realPath) {
		boolean result = false;

		File tmpFileNamePath = new File(realPath);
		File[] files = tmpFileNamePath.listFiles();
		for (File f : files) {
			if (f.getName().equals(tmpFileName)) { // 파일명이 중복 된다.
				result = true;
			}
		}

		return result;

	}

	private String makeNewUniqueFileName(String userId, String tmpFileName) {
		String newFileName = "";
		String ext = tmpFileName.substring(tmpFileName.lastIndexOf(".") + 1);
		String uuid = UUID.randomUUID().toString();

		newFileName = userId + "_" + uuid + "." + ext;

		System.out.println("저장될 새로운 파일 이름 : " + newFileName);

		return newFileName;
	}

}
