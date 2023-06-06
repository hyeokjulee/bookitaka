package com.bookitaka.NodeulProject.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailSender {
    @Value("${spring.mail.username}")
    private String fromEmail;  // 네이버 이메일 주소
    private final JavaMailSender javaMailSender;

    public boolean sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            javaMailSender.send(message);
            log.info("Email sent successfully!");
            return true;
        } catch (MailException e) {
            e.printStackTrace();
            log.info("Email sent fail!");
        }
        return false;
    }

    public boolean sendEmailWithTemplate(String toEmail, String templateName, String subject, String content) {
        try {
            // HTML 템플릿 파일 읽기
            Path templatePath = Paths.get(new ClassPathResource(templateName).getURI());
            String htmlContent = new String(Files.readAllBytes(templatePath), StandardCharsets.UTF_8);

            // 변수 값 채워넣기
            htmlContent = htmlContent.replace("[[subject]]", subject);
            htmlContent = htmlContent.replace("[[content]]", content);

            // 이메일 메시지 생성
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());
            helper.setFrom(fromEmail);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            // 이메일 전송
            javaMailSender.send(message);

            log.info("Email sent successfully!");
            return true;
        } catch (MessagingException | MailException | IOException e) {
            e.printStackTrace();
            log.info("Email sent fail!");
        }

        return false;
    }
}