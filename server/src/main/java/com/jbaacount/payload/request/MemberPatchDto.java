package com.jbaacount.payload.request;

import com.jbaacount.global.validation.notspace.NotSpace;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberPatchDto
{
    @Length(min = 3, max = 10, message = "닉네임은 3자 이상 10자 이하여야 합니다.")
    @Pattern(regexp = "[A-z가-힣0-9 ]{3,10}", message = "닉네임에는 특수문자 및 공백이 올 수 없습니다.")
    @NotSpace
    private String nickname;

    @Length(min = 5, max = 20, message = "비밀번호는 5자 이상 20자 이하여야 합니다.")
    private String password;

}
