package com.jbaacount.global.exception;

import lombok.Getter;

public enum ExceptionMessage
{
    /**
     * member
     */
    USER_NOT_FOUND("The specified user could not be found"),
    EMAIL_ALREADY_EXIST("The provided email is already in use"),
    NICKNAME_ALREADY_EXIST("The provided nickname is already in use"),

    MEMBER_UNAUTHORIZED("This user has no authority"),

    /**
     * token
     */

    EXPIRED_TOKEN("The JWT token has expired"),
    TOKEN_NOT_VALID("The provided JWT token is not valid"),

    UNSUPPORTED_TOKEN("The JWT token is not supported"),

    CLAIM_EMPTY("The JWT claims string is empty"),

    INVALID_TOKEN_SIGNATURE("The signature of your token is invalid"),
    TOKEN_NOT_FOUND("The refresh token could not be located in Redis");


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
