package com.bookitaka.NodeulProject.member.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
@Slf4j
public class EmailService {
    @Value("${naverID}")
    private String username;  // 네이버 이메일 주소
    @Value("${naverPW}")
    private String password;  // 네이버 이메일 비밀번호
    public void sendEmail(String email,String pw) {
        // 네이버 SMTP 서버 설정
        String host = "smtp.naver.com";
        int port = 465;


        // 이메일 속성 설정
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.enable", "true");
        log.info("==================== naverid:{}",username);
        log.info("==================== naverpw:{}",password);
        // 세션 생성 및 인증
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // 메시지 작성
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));  // 수신자 이메일 주소
            message.setSubject("북키타카 임시비밀번호 발송");
            message.setText("북키타카 임시 비밀번호 발송"+pw);

            // 이메일 전송
            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}