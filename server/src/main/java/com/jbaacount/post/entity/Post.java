package com.jbaacount.post.entity;

import com.jbaacount.board.entity.Board;
import com.jbaacount.category.entity.Category;
import com.jbaacount.comment.entity.Comment;
import com.jbaacount.file.entity.File;
import com.jbaacount.global.audit.BaseEntity;
import com.jbaacount.member.entity.Member;
import com.jbaacount.vote.entity.Vote;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Post extends BaseEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;

    @Column(nullable = false)
    private int voteCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<File> files = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Vote> votes = new ArrayList<>();
    @Builder
    public Post(String title, String content)
    {
        this.title = title;
        this.content = content;
        this.voteCount = 0;
    }

    public void addMember(Member member)
    {
        if(this.member != null)
            this.member.getPosts().remove(this);

        this.member = member;
        member.getPosts().add(this);
    }

    public void addCategory(Category category)
    {
        if(this.category != null)
            this.category.getPosts().remove(this);

        this.category = category;
        category.getPosts().add(this);
    }

    public void addBoard(Board board)
    {
        if(this.board != null)
            this.board.getPosts().remove(this);

        this.board = board;
        board.getPosts().add(this);
    }

    public void updateTitle(String title)
    {
        this.title = title;
    }

    public void updateContent(String content)
    {
        this.content = content;
    }

    public void upVote()
    {
        this.voteCount++;
    }

    public void downVote()
    {
        this.voteCount--;
    }

    public void removeFile(File file)
    {
        this.files.remove(file);
    }
}
