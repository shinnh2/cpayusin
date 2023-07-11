package com.jbaacount.member.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
@AllArgsConstructor
@Data
public class MemberPatchDto
{
    private Long id;

    @Length(min = 3, max = 10, message = "닉네임은 3자 이상 10자 이하여야 합니다.")
    @Pattern(regexp = "[A-z가-힣0-9]", message = "닉네임에는 특수문자 및 공백이 올 수 없습니다.")
    private String nickname;

    @Length(min = 5, max = 20, message = "비밀번호는 5자 이상 20자 이하여야 합니다.")
    private String password;

}
