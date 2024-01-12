package com.jbaacount.service;

import com.jbaacount.repository.RedisRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Random;

@RequiredArgsConstructor
@Slf4j
@Service
public class MailService
{
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final RedisRepository redisRepository;
    private final String WORDS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public String sendVerificationCode(String email)
    {
        String verificationCode = generateVerificationCode();
        redisRepository.saveEmailAndVerificationCodeWith5Minutes(email, verificationCode);

        String response = sendMail(email, verificationCode);

        return response;
    }

    private String sendMail(String email, String verificationCode)
    {
        Context context = new Context();
        context.setVariable("verificationCode", verificationCode);
        String htmlContent = templateEngine.process("mailTemplate", context);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try{
            helper.setTo(email);
            helper.setSubject("이메일 인증코드입니다.");
            helper.setText(htmlContent, true);
        } catch (MessagingException e){
            e.printStackTrace();
        }

        mailSender.send(mimeMessage);

        log.info("mail verification code = {}", verificationCode);
        log.info("email = {}", email);

        return "인증코드가 발송되었습니다. 5분 내로 인증을 완료해주세요.";
    }

    private String generateVerificationCode()
    {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 8; i++)
        {
            sb.append(WORDS.charAt(random.nextInt(WORDS.length())));
        }
        String verificationCode = sb.toString();

        log.info("verification code = {}", verificationCode);

        return verificationCode;
    }
}
