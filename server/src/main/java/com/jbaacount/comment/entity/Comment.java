package com.jbaacount.comment.entity;

import com.jbaacount.member.entity.Member;
import com.jbaacount.post.entity.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Comment
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parent;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @Builder
    public Comment(String text)
    {
        this.text = text;
    }

    public void addPost(Post post)
    {
        if(this.post != null)
            this.post.getComments().remove(this);

        this.post = post;
        post.getComments().add(this);
    }

    public void addParent(Comment parent)
    {
        if(this.parent != null)
            this.parent.getChildren().remove(this);

        this.parent = parent;
        parent.getChildren().add(this);
    }

    public void addMember(Member member)
    {
        this.member = member;
    }

    public void updateText(String text)
    {
        this.text = text;
    }
}
