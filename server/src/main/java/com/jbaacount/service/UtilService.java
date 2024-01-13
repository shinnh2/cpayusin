package com.jbaacount.service;

import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.model.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;

@Transactional(readOnly = true)
@Service
public class UtilService
{
    public void checkPermission(Long memberId, Member currentMember)
    {
        if (currentMember.getRoles().contains("ADMIN"))
            return;

        isTheSameUser(memberId, currentMember.getId());
    }

    public void isTheSameUser(Long memberId, Long loggedInMemberId)
    {
        if(memberId != loggedInMemberId)
            throw new BusinessLogicException(ExceptionMessage.MEMBER_UNAUTHORIZED);
    }

    public void isAdmin(Member currentMember)
    {
        if(!currentMember.getRoles().contains("ADMIN"))
            throw new BusinessLogicException(ExceptionMessage.MEMBER_UNAUTHORIZED);
    }

    public void isUserAllowed(Boolean isAdminOnly, Member currentMember)
    {
        if(isAdminOnly)
            isAdmin(currentMember);
    }

    public static String calculateTime(LocalDateTime time)
    {
        LocalDateTime currentTime = LocalDateTime.now();
        Duration duration = Duration.between(time, currentTime);

        long seconds = duration.getSeconds();

        if(seconds < 60)
            return "방금 전";

        else if(seconds < 3600)
            return seconds / 60 + "분 전";

        else if(seconds < 86400)
            return seconds / 3600 + "시간 전";

        else
        {
            Period period = Period.between(time.toLocalDate(), currentTime.toLocalDate());

            if(period.getYears() > 0)
                return period.getYears() + "년 전";

            else if(period.getMonths() > 0)
                return period.getMonths() + "달 전";

            else if(period.getDays() >= 7)
                return period.getDays() / 7 + "주 전";

            else return period.getDays() + "일 전";
        }
    }
}
