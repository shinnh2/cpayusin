package com.jbaacount.domain;

import com.jbaacount.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Setter
@AllArgsConstructor @Builder
@NoArgsConstructor
@Getter
@Entity
public class Member extends BaseEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nickname;

    @Column(updatable = false, unique = true)
    private String email;

    private String password;

    private String authority;




    public void updateAuthority(String authority)
    {
        this.authority = authority;
    }

    public void updatePassword(String password)
    {
        this.password = password;
    }

    public void updateNickname(String nickname)
    {
        this.nickname = nickname;
    }
}
