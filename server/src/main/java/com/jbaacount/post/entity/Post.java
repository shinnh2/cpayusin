package com.jbaacount.post.entity;

import com.jbaacount.category.entity.Category;
import com.jbaacount.global.audit.BaseEntity;
import com.jbaacount.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Category category;

    @Builder
    public Post(String title, String content)
    {
        this.title = title;
        this.content = content;
    }

    public void addMember(Member member)
    {
        if(this.member != null)
            this.member.getPosts().remove(this);

        this.member = member;

        if(member.getPosts() == null)
            member.setPosts(new ArrayList<>());

        member.getPosts().add(this);
    }

    public void addCategory(Category category)
    {
        if(this.category != null)
            this.category.getPosts().remove(this);

        this.category = category;

        if(category.getPosts() == null)
            category.setPosts(new ArrayList<>());

        category.getPosts().add(this);
    }

    public void updateTitle(String title)
    {
        this.title = title;
    }

    public void updateContent(String content)
    {
        this.content = content;
    }
}
