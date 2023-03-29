package com.miniproj.member.controller;

import com.miniproj.member.service.ConfirmCodeService;
import com.miniproj.member.service.DuplicateUserIdService;
import com.miniproj.member.service.LoginMemberService;
import com.miniproj.member.service.LogoutMemberService;
import com.miniproj.member.service.MemberService;
import com.miniproj.member.service.MyPageMemberService;
import com.miniproj.member.service.RegisterMemberService;
import com.miniproj.member.service.SendMailService;

public class MemberFactory {
	private static MemberFactory instance = null;
	private boolean isRedirect; // redirect 할 것인지 말 것인지
	private String whereIsgo; // 어느 페이지로 이동할 것이냐
	
	private MemberFactory() {
	}

	public boolean isRedirect() {
		return isRedirect;
	}

	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}

	public String getWhereIsgo() {
		return whereIsgo;
	}

	public void setWhereIsgo(String whereIsgo) {
		this.whereIsgo = whereIsgo;
	}

	public static MemberFactory getInstance() {
		if (instance == null) {
			instance = new MemberFactory();
		}
		return instance;
	}

	/**
	 * 공통서블릿에서 command를 받아 필요한 서비스단의 객체를 반환해주는 메소드
	 * 
	 * @param command : 공통서블릿단에서 주는 요청한 서비스 객체
	 */
	public MemberService getService(String command) {
		MemberService service = null;
		if (command.equals("/member/login.mem")) {
			service = new LoginMemberService();
		} else if (command.equals("/member/register.mem")) {
			service = new RegisterMemberService();
		} else if (command.equals("/member/duplicateUserId.mem")) {
			service = new DuplicateUserIdService();
		} else if (command.equals("/member/logout.mem")) {
			service = new LogoutMemberService();
		} else if (command.equals("/member/sendMail.mem")) {
			service = new SendMailService();
		} else if (command.equals("/member/confirmCode.mem")){
			service = new ConfirmCodeService();
		}else if (command.equals("/member/myPage.mem")) {  // 마이 페이지
			service = new MyPageMemberService();
		}
		return service;
	}

}
