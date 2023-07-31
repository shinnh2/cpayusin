package com.jbaacount.comment.entity;

import com.jbaacount.global.audit.BaseEntity;
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
public class Comment extends BaseEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private int voteCount;

    @Column(nullable = false)
    private boolean isRemoved = false;

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
        this.voteCount = 0;
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
        if(this.member != null)
            this.member.getComments().remove(this);

        this.member = member;
        member.getComments().add(this);
    }

    public void updateText(String text)
    {
        this.text = text;
    }
    public void deleteComment()
    {
        this.text = "삭제된 댓글입니다.";
        this.isRemoved = true;
    }

    public void upVote()
    {
        this.voteCount++;
    }

    public void downVote()
    {
        this.voteCount--;
    }
}
