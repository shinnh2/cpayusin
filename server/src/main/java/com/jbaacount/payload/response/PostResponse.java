package com.jbaacount.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse
{
    private Long boardId;
    private String boardName;

    private Long categoryId;
    private String categoryName;

    private Long memberId;
    private String memberName;

    private Long postId;
    private String title;
    private String content;
    private Integer voteCount;
    private Integer commentCount;
    private LocalDateTime createdAt;

}
