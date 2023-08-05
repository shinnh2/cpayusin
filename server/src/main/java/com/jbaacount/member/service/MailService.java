package com.jbaacount.member.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.member.entity.Member;
import com.jbaacount.member.repository.MemberRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDateTime;
import java.util.UUID;

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

    @Transactional
    public String sendMail(String to)
    {
        Member member = findMemberByEmail(to);

        String verificationCode = UUID.randomUUID().toString().substring(0, 8);
        member.setVerificationCode(verificationCode);

        Context context = new Context();
        context.setVariable("verificationCode", verificationCode);
        String htmlContent = templateEngine.process("mailTemplate", context);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try{
            helper.setTo(to);
            helper.setSubject("이메일 인증코드입니다.");
            helper.setText(htmlContent, true);
        } catch (MessagingException e){
            e.printStackTrace();
        }

        mailSender.send(mimeMessage);

        log.info("mail verification code = {}", verificationCode);
        log.info("email = {}", to);

        return verificationCode;
    }

    public boolean verifyCode(String email, String inputCode)
    {
        Member member = findMemberByEmail(email);

        if((member.getVerificationCode().equals(inputCode)) && (member.getVerificationCodeExpiry().isAfter(LocalDateTime.now())))
        {
            return true;
        }

        throw new BusinessLogicException(ExceptionMessage.INVALID_VERIFICATION_CODE);
    }

    @Transactional
    public Member resetPassword(String email, String password)
    {
        Member member = findMemberByEmail(email);

        member.updatePassword(passwordEncoder.encode(password.toString()));

        return member;
    }

    private Member findMemberByEmail(String email)
    {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessLogicException(ExceptionMessage.USER_NOT_FOUND));
    }
}
