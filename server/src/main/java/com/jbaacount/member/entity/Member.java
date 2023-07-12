package com.jbaacount.member.entity;

import com.jbaacount.global.audit.BaseEntity;
import com.jbaacount.post.entity.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@AllArgsConstructor @Builder
@NoArgsConstructor
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

    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    private List<Post> posts = new ArrayList<>();
    public void updatePassword(String password)
    {
        this.password = password;
    }

    public void updateNickname(String nickname)
    {
        this.nickname = nickname;
    }
}
