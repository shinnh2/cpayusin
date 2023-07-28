package com.jbaacount.member.entity;

import com.jbaacount.global.audit.BaseEntity;
import com.jbaacount.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends BaseEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nickname;

    @Column(updatable = false, unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @Builder
    public Member(String nickname, String email, String password)
    {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
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
