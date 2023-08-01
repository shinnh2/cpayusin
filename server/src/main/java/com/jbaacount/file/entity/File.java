package com.jbaacount.file.entity;

import com.jbaacount.global.audit.BaseEntity;
import com.jbaacount.post.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class File extends BaseEntity
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String uploadFileName;

    @Column(nullable = false)
    private String storeFileName;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String contentType;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder
    public File(String uploadFileName, String storeFileName, String url, String contentType)
    {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.url = url;
        this.contentType = contentType;
    }

    public void addPost(Post post)
    {
        if(this.post != null)
            this.post.getFiles().remove(this);

        this.post = post;
        post.getFiles().add(this);
    }
}
