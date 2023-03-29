package com.miniproj.etc;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

	public static void send(String userEmailAddr, String comfirmCode) throws MessagingException, MessagingException, NoSuchProviderException {
		// 이메일을 발송
		// SMTP (Send Mail Transfer Protocol : 메일 전송 통신 규약
		Properties prop = new Properties();
		
		// 보낼 메일을 만들기
		String subject = "miniproj.com에서 보낸 이메일 인증 번호입니다";
		String Message = "miniproj.com 회원 가입을 완영합니다. 회원가입 화면에서 아래의 인증 번호 : "
								+ comfirmCode + "를 입력하시고 확인 버튼을 눌러 인증을 해주세요."; 
		
		
		prop.put("mail.smtp.starttls.required", "ture"); // 메일 서버 환경설정 시작
		prop.put("mail.smtp.ssl.protocols", "TLSv1.2"); // 보안 프로토콜 사용 선언
		prop.put("mail.smtp.host", "smtp.gmail.com"); // smtp 서버명
		prop.put("mail.smtp.port", "465"); // 포트번호
		prop.put("mail.smtp.auth", "true"); // 인증 과정을 거치겠다
		prop.put("mail.smtp.ssl.enable", "true"); // SSL 사용하겠다.
		
		// 인증과정
		Session mailSession = Session.getInstance(prop, new Authenticator() { // Authenticator 객체 : 내 인증 정보를 가지고 있음

			// 2) 메일 서버에 javax.mail.PasswordAuthentication 객체로 로그인하여 
			// javax.mail.PasswordAuthentication 타입의 인증 정보 토큰을 반환
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// 1) 내 메일 서버의 아이디와 패스워드 정보를 객체로 만들어서 반환
				return new javax.mail.PasswordAuthentication(EmailAccount.emailAddr, EmailAccount.emailPwd);
			}
			
		});
		
		System.out.println(mailSession.toString());
		
		if (mailSession != null) {
			MimeMessage mime = new MimeMessage(mailSession);
			
			
				// 메일 보내는 사람 주소 세팅
				mime.setFrom(new InternetAddress("haena0225@gmail.com"));
				// 받는 사람 주소
				mime.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(userEmailAddr));
				mime.setSubject(subject); // 제목
				
				String html = "<h5> 메일 인증 코드 : <a href='http://localhost:8082/MiniProject/member/register.jsp?cc=" + comfirmCode + "'>"
						+ comfirmCode + "</a></h5>";
				
				mime.setText(Message); // 본문
				mime.setText(html, "utf-8", "html");
				// mime.setContent(new Multipart); // 파일 첨부시 사용

			
				// 실제로 메일 보내기
				Transport tran = mailSession.getTransport("smtp");
				tran.connect(EmailAccount.emailAddr, EmailAccount.emailPwd);
				tran.sendMessage(mime, mime.getAllRecipients());
				tran.close();
				
		}
		
		
	}
}
