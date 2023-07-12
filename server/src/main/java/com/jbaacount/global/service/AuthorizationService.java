package com.jbaacount.global.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.member.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class AuthorizationService
{
    public void checkPermission(Long memberId, Member currentMember)
    {
        if (currentMember.getRoles().contains("ADMIN"))
            return;

        isTheSameUser(memberId, currentMember.getId());
    }

    private void isTheSameUser(Long memberId, Long loggedInMemberId)
    {
        if(memberId != loggedInMemberId)
            throw new BusinessLogicException(ExceptionMessage.MEMBER_UNAUTHORIZED);
    }
}
