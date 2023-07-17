package com.jbaacount.board.dto.response;

import com.jbaacount.post.dto.response.PostInfoForResponse;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@AllArgsConstructor
public class BoardResponseDto
{
    private Long id;
    private String name;
}
