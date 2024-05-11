package com.jbaacount.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.model.Member;
import com.jbaacount.model.type.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Random;

@Transactional(readOnly = true)
@Service
public class UtilService
{
    private static final String WORDS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public void checkPermission(Long memberId, Member currentMember)
    {
        if (Role.ADMIN.getValue().equals(currentMember.getRole()))
            return;

        isTheSameUser(memberId, currentMember.getId());
    }

    public void isTheSameUser(Long memberId, Long loggedInMemberId)
    {
        if(memberId.longValue() != loggedInMemberId.longValue())
            throw new BusinessLogicException(ExceptionMessage.MEMBER_UNAUTHORIZED);
    }

    public void isAdmin(Member currentMember)
    {
        if(!currentMember.getRole().equals(Role.ADMIN.getValue()))
            throw new BusinessLogicException(ExceptionMessage.MEMBER_UNAUTHORIZED);
    }

    public void isUserAllowed(Boolean isAdminOnly, Member currentMember)
    {
        if(isAdminOnly)
            isAdmin(currentMember);
    }

    public static String generateVerificationCode()
    {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 8; i++)
        {
            sb.append(WORDS.charAt(random.nextInt(WORDS.length())));
        }
        String verificationCode = sb.toString();

        return verificationCode;
    }
}
