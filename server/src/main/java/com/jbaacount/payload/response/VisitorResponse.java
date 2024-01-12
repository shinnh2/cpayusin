package com.jbaacount.payload.response;

import lombok.Builder;
import lombok.Data;

@Data
public class VisitorResponse
{
    private Long yesterday;
    private Long today;
    private Long total;

    @Builder
    public VisitorResponse(Long yesterday, Long today, Long total)
    {
        this.yesterday = yesterday;
        this.today = today;
        this.total = total;
    }
}
