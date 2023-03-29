package com.miniproj.member.controller;

import com.miniproj.member.service.ConfirmCodeService;
import com.miniproj.member.service.DuplicateUserIdService;
import com.miniproj.member.service.GetMemberPointService;
import com.miniproj.member.service.LoginMemberService;
import com.miniproj.member.service.LogoutMemberService;
import com.miniproj.member.service.MemberService;
import com.miniproj.member.service.MyPageMemberService;
import com.miniproj.member.service.RegisterMemberService;
import com.miniproj.member.service.SendMailService;

public class MemberFactory {
	private static MemberFactory instance = null;
	private boolean isRedirect;  // redirect 할것인지 말것인지
	private String whereisgo; // 어느 페이지로 이동할것이냐
	
	private MemberFactory() { }
	
	public static MemberFactory getInstance() {
		if (instance == null) {
			instance = new MemberFactory();
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

	/**
	 * 공통서블릿에서 command를 받아 필요한 서비스단의 객체를 반환해주는 메서드
	 * 
	 * @param command : 공통서블릿단에서 주는 요청한 서비스 객체
	 * 반환값 타입 : 
	 */
	public MemberService getService(String command) {
		MemberService service = null;
		
		if (command.equals("/member/login.mem")) {  // 로그인
			service = new LoginMemberService();
		} else if (command.equals("/member/register.mem")) { // 회원가입
			service = new RegisterMemberService();
		} else if (command.equals("/member/duplicateUserId.mem")) { // 회원 아이디 중복 검사
			service = new DuplicateUserIdService();
		} else if (command.equals("/member/logout.mem")) { // 로그인
			service = new LogoutMemberService();
		} else if (command.equals("/member/sendMail.mem")) {
			service = new SendMailService();  // 인증코드를 메일로 보내주는 서비스
		} else if (command.equals("/member/confirmCode.mem")) { // 인증코드 확인서비스
			service = new ConfirmCodeService();
		} else if (command.equals("/member/myPage.mem")) {  // 마이 페이지
			service = new MyPageMemberService();
		} else if (command.equals("/member/getMemPoint.mem")) {
			service = new GetMemberPointService();
		}
		
		
		return service;
	}
}
