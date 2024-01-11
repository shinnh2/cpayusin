package com.jbaacount.utils;

import com.jbaacount.model.Member;
import com.jbaacount.service.MemberService;

import java.lang.reflect.Field;

public class TestUtil
{
    private static final String adminEmail = "mike@ticonsys.com";
    private static final String userEmail = "aaa@naver.com";
    public static Member createAdminMember(MemberService memberService)
    {
        Member admin = Member.builder()
                .nickname("관리자")
                .email(adminEmail)
                .password("123123")
                .build();
        return memberService.createMember(admin);
    }

    public static Member createUserMember(MemberService memberService)
    {
        Member user = Member.builder()
                .nickname("유저")
                .email(userEmail)
                .password("123123")
                .build();

        return memberService.createMember(user);
    }

    public static void setFieldsForEntity(Object targetObject, String fieldName, Object value) {
        try {
            Field field = getField(targetObject.getClass(), fieldName);

            if (field == null) {
                throw new NoSuchFieldException("Field " + fieldName + " not found on " + targetObject.getClass());
            }

            field.setAccessible(true);  // make it accessible
            field.set(targetObject, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set field using reflection", e);
        }
    }

    private static Field getField(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() != null) {
                return getField(clazz.getSuperclass(), fieldName);
            }
            return null;
        }
    }


}
