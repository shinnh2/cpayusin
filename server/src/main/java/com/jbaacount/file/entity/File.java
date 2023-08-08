package com.jbaacount.file.entity;

import com.jbaacount.global.audit.BaseEntity;
import com.jbaacount.member.entity.Member;
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

    @Column(nullable = false, unique = true)
    private String storeFileName;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String contentType;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

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

    public void addMember(Member member)
    {
        this.member = member;
        member.setFile(this);
    }
}
