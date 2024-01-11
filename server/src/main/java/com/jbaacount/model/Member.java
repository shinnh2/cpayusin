package com.jbaacount.model;

import com.jbaacount.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verification_code_expiry")
    private LocalDateTime verificationCodeExpiry;

    @Column(nullable = false, name = "score")
    private int score;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private Platform platform;

    @OneToMany(mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Comment> comments = new ArrayList<>();

    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.EAGER)
    private File file;

    @Builder
    public Member(String nickname, String email, String password)
    {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.platform = Platform.HOME;
        this.score = 0;
    }

    public Member(String nickname, String email, List<String> roles, Platform platform)
    {
        this.nickname = nickname;
        this.password = UUID.randomUUID().toString();
        this.email = email;
        this.roles = roles;
        this.platform = platform;
        this.score = 0;
    }

    public void updatePassword(String password)
    {
        this.password = password;
    }

    public void updateNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public void setVerificationCode(String verificationCode)
    {
        this.verificationCode = verificationCode;
        this.verificationCodeExpiry = LocalDateTime.now().plusMinutes(5);
    }

    public void setFile(File file)
    {
        this.file = file;
    }

    public void getScoreByVote()
    {
        this.score += 2;
    }

    public void getScoreByPost()
    {
        this.score += 3;
    }

    public void getScoreByComment()
    {
        this.score += 1;
    }

    public void initializeScore()
    {
        this.score = 0;
    }
}
