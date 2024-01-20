package com.jbaacount.model;

import com.jbaacount.global.audit.BaseEntity;
import com.jbaacount.model.type.CommentType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Comment extends BaseEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String text;

    @Column(nullable = false)
    private boolean isRemoved = false;

    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "parent")
    private List<Comment> children = new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();

    @Builder
    public Comment(String text)
    {
        this.text = text;
        this.type = CommentType.PARENT_COMMENT.getCode();
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

}
