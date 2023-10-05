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

    @Column(nullable = false)
    private Boolean isAdminOnly;

    @Column(nullable = false)
    private Long orderIndex;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Category> categories = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
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

    public void changeBoardAuthority(Boolean isAdminOnly)
    {
        this.isAdminOnly = isAdminOnly;
    }

    public void updateOrderIndex(Long orderIndex)
    {
        this.orderIndex = orderIndex;
    }
}
