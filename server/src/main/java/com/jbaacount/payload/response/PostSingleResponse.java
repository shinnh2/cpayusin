package com.jbaacount.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostSingleResponse
{
    private Long memberId;
    private Long boardId;
    private String nickname;

    @JsonProperty("postId")
    private Long id;
    private String title;
    private String content;
    private List<FileResponse> files;

    private Integer voteCount;
    private boolean voteStatus;
    private String createdAt;
}
