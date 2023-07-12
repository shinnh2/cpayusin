package com.jbaacount.post.entity;

import com.jbaacount.global.audit.BaseEntity;
import com.jbaacount.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Post extends BaseEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Post(String title, String content, Member member)
    {
        this.title = title;
        this.content = content;
    }

    public void addMember(Member member)
    {
        if(this.member != null)
            this.member.getPosts().remove(this);

        this.member = member;
        member.getPosts().add(this);
    }
}
