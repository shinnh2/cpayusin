package com.jbaacount.payload.request.member;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class PasswordResetRequest
{
    @Length(min = 5, max = 20, message = "비밀번호는 5자 이상 20자 이하여야 합니다.")
    private String password;

}
