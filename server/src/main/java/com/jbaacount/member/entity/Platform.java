package com.jbaacount.member.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Platform
{
    HOME("HOME"),
    GOOGLE("GOOGLE"),
    NAVER("NAVER"),
    KAKAO("KAKAO");

    private final String value;

    Platform(String value)
    {
        this.value = value;
    }

    @JsonValue
    public String getValue()
    {
        return value;
    }
}
