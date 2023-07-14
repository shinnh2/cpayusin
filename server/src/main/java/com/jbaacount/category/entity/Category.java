package com.jbaacount.category.entity;

import com.jbaacount.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Entity
public class Category
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "category")
    private List<Post> posts = new ArrayList<>();

    public void setPosts(List<Post> posts)
    {
        this.posts = posts;
    }
}
