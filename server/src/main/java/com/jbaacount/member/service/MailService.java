package com.jbaacount.member.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@RequiredArgsConstructor
@Slf4j
@Service
public class MailService
{
    //private final MailSender mailSender;
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final SpringTemplateEngine templateEngine;
    private final JavaMailSender mailSender;
    private final RedisTemplate<String, String> redisTemplate;
    static String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @Transactional
    public String sendMailForRestPassword(String to)
    {
        Member member = findMemberByEmail(to);

        String verificationCode = generateVerificationCode();
        member.setVerificationCode(verificationCode);

        return sendMail(to, verificationCode);
    }

    public boolean verifyCodeForResetPassword(String email, String inputCode)
    {
        Member member = findMemberByEmail(email);

        if((member.getVerificationCode().equals(inputCode)))
        {
            if(member.getVerificationCodeExpiry().isAfter(LocalDateTime.now()))
            {
                member.setVerificationCode(null);
                member.setVerificationCodeExpiry(null);
                return true;
            }

            else
            {
                throw new BusinessLogicException(ExceptionMessage.EXPIRED_VERIFICATION_CODE);
            }
        }

        throw new BusinessLogicException(ExceptionMessage.INVALID_VERIFICATION_CODE);
    }

    public String sendMailForSignUp(String email)
    {
        String verificationCode = generateVerificationCode();
        redisTemplate.opsForValue().set(email, verificationCode, Duration.ofMinutes(5));

        return sendMail(email, verificationCode);
    }

    public boolean verifyCodeForSignUp(String email, String inputCode)
    {
        String verificationCode = redisTemplate.opsForValue().get(email);

        if(verificationCode == null)
            throw new BusinessLogicException(ExceptionMessage.EXPIRED_VERIFICATION_CODE);

        if(verificationCode.equals(inputCode))
            return true;

        throw new BusinessLogicException(ExceptionMessage.INVALID_VERIFICATION_CODE);
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

        return verificationCode;
    }

    @Transactional
    public Member resetPassword(String email, String password)
    {
        Member member = findMemberByEmail(email);

        member.updatePassword(passwordEncoder.encode(password.toString()));

        return member;
    }

    private String generateVerificationCode()
    {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 8; i++)
        {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        String verificationCode = sb.toString();

        log.info("verification code = {}", verificationCode);

        return verificationCode;
    }

    private Member findMemberByEmail(String email)
    {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionMessage.USER_NOT_FOUND));
    }
}
