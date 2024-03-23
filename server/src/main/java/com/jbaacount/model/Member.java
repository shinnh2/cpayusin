package com.jbaacount.model;

import com.jbaacount.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@ToString
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
    public Member(Long id, String nickname, String email, String password, int score, List<String> roles, Platform platform, List<Post> posts, List<Comment> comments, File file)
    {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.score = 0;
        this.roles = roles;
        this.platform = Platform.HOME;
        this.posts = posts;
        this.comments = comments;
        this.file = file;
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


    public void setFile(File file)
    {
        this.file = file;
    }

    public void getScoreByVote(int num)
    {
        this.score += num;
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
