package com.jbaacount.global.exception;

import lombok.Getter;

public enum ExceptionMessage
{
    /**
     * member
     */
    USER_NOT_FOUND("유저를 찾을 수 없습니다."),
    EMAIL_ALREADY_EXIST("이미 존재하는 이메일입니다."),
    NICKNAME_ALREADY_EXIST("이미 존재하는 닉네임입니다"),


    /**
     * token
     */

    TOKEN_EXPIRED("이미 만료된 토큰입니다."),
    TOKEN_NOT_VALID("유효하지 않은 토큰입니다.");




    @Getter
    private final String message;


    ExceptionMessage(String message)
    {
        this.message = message;
    }

    public String get()
    {
        return message;
    }
}
