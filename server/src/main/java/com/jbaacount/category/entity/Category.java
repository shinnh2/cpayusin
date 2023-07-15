package com.jbaacount.category.entity;

import com.jbaacount.global.audit.BaseEntity;
import com.jbaacount.post.entity.Post;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Category extends BaseEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean isAdminOnly;

    @Builder
    public Category(String name, boolean isAdminOnly)
    {
        this.name = name;
        this.isAdminOnly = isAdminOnly;
    }

    @OneToMany(mappedBy = "category")
    private List<Post> posts = new ArrayList<>();

    public void setPosts(List<Post> posts)
    {
        this.posts = posts;
    }

    public void changeCategoryAuthority(boolean adminOnly)
    {
        isAdminOnly = adminOnly;
    }
}
