package com.jbaacount.board.entity;

import com.jbaacount.category.entity.Category;
import com.jbaacount.global.audit.BaseEntity;
import com.jbaacount.post.entity.Post;
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
public class Board extends BaseEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Boolean isAdminOnly;

    @OneToMany(mappedBy = "board")
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "board")
    private List<Post> posts = new ArrayList<>();

    @Builder
    public Board(String name, Boolean isAdminOnly)
    {
        this.name = name;
        this.isAdminOnly = isAdminOnly;
    }

    public void updateName(String name)
    {
        this.name = name;
    }

    public void changeBoardAuthority(Boolean adminOnly)
    {
        isAdminOnly = adminOnly;
    }
}
