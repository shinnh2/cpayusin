package com.jbaacount.visitor.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class VisitorResponseDto
{
    private Long yesterday;
    private Long today;
    private Long total;

    @Builder
    public VisitorResponseDto(Long yesterday, Long today, Long total)
    {
        this.yesterday = yesterday;
        this.today = today;
        this.total = total;
    }
}
