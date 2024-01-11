package com.jbaacount.payload.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
public class PostMultiResponseDto
{
    private Long boardId;
    private String boardName;
    private Long categoryId;
    private String categoryName;
    private BoardResponse board;
    private CategoryResponse category;
    private MemberResponse member;
    private Long id;
    private String title;
    private String content;
    private Integer voteCount;
    private Integer commentCount;
    private LocalDateTime createdAt;

    @QueryProjection
    public PostMultiResponseDto(BoardResponse board, CategoryResponse category, MemberResponse member, Long id, String title, String content, Integer voteCount, Integer commentCount, LocalDateTime createdAt)
    {
        this.board = board;
        this.category = category;
        this.member = member;
        this.id = id;
        this.title = title;
        this.content = content;
        this.voteCount = voteCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class BoardResponse
    {
        private Long id;
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class CategoryResponse
    {
        private Long id;
        private String name;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class MemberResponse
    {
        private Long id;
        private String nickname;
    }
}
