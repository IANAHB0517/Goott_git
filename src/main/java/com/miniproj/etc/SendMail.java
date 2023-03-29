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
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.mysql.cj.protocol.Message;

public class SendMail {

	public static void send(String userEmailAddr, String confirmCode)
			throws AddressException, MessagingException, NoSuchProviderException {
		// SMTP(Send Mail Transfer Protocol) : 메일 전송 통신 규약

		Properties prop = new Properties();

		// 보낼 메일을 만듦
		String subject = "miniproj.com에서 보낸 이메일 인증번호입니다!";
		String message = "miniproj.com 회원 가입을 환영합니다. 회원 가입 화면에서 인증번호 : " + confirmCode + "를 입력하시고 확인 버튼을 눌러 인증을 해주세요~";

		// 메일 환경 세팅
		// naver
//		prop.put("mail.smtp.starttls.required", "true"); // 메일 서버 환경설정 시작
//		prop.put("mail.smtp.ssl.protocols", "TLSv1.2"); // 보안프로토콜 사용
//		prop.put("mail.smtp.host", "smtp.naver.com"); // smtp서버명
//		prop.put("mail.smtp.port", "465"); // 포트번호
//		prop.put("mail.smtp.auth", "true"); // 인증 과정을 거치겠다
//		prop.put("mail.smtp.ssl.enable", "true"); // SSL 사용
		
		// gmail
		prop.put("mail.smtp.starttls.required", "true"); // 메일 서버 환경설정 시작
		prop.put("mail.smtp.ssl.protocols", "TLSv1.2"); // 보안프로토콜 사용
		prop.put("mail.smtp.host", "smtp.gmail.com"); // smtp서버명
		prop.put("mail.smtp.port", "465"); // 포트번호
		prop.put("mail.smtp.auth", "true"); // 인증 과정을 거치겠다
		prop.put("mail.smtp.ssl.enable", "true"); // SSL 사용

		// 인증 과정
		Session mailSession = Session.getInstance(prop, new Authenticator() {

			// 2) 메일서버에 javax.mail.PasswordAuthentication 객체로 로그인 하여
			// javax.mail.PasswordAuthentication 타입의 인증 정보 토큰을 반환
			@Override
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				// 1) 내 메일서버의 아이디와 패스워드 정보를 객체로 만들어서 반환
				return new javax.mail.PasswordAuthentication(EmailAccount.emailAddr, EmailAccount.emailPwd);
			}

		});

		System.out.println(mailSession.toString());

		if (mailSession != null) {
			MimeMessage mime = new MimeMessage(mailSession);

			mime.setFrom(new InternetAddress("rs2225@naver.com")); // 보내는사람
			// 받는 사람
			mime.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(userEmailAddr));
			mime.setSubject(subject); // 제목
			
//				mime.setContent(new Multipart() {
//					
//					@Override
//					public void writeTo(OutputStream arg0) throws IOException, MessagingException {
//						// TODO Auto-generated method stub
//						
//					}
//				}); // 파일 첨부시
			
			String html = 
					"<h5>메일 인증 코드 : " + 
							"<a href='http://localhost:8082/MiniProject/member/register.jsp?cc=" + confirmCode + "'>" 
							+ confirmCode + "</a></h5>";
			
//			mime.setContent(html, "text/html;");
			
			mime.setText(html, "utf-8", "html"); // 본문

			Transport tran = mailSession.getTransport("smtp");
			tran.connect(EmailAccount.emailAddr, EmailAccount.emailPwd);
			tran.sendMessage(mime, mime.getAllRecipients());
			tran.close();

		}

	}
}
