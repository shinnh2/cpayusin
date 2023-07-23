package com.jbaacount.global.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageDto<T>
{
    private List<T> data;
    private PageInfo pageInfo;

    public PageDto(Page<T> page)
    {
        this.data = page.getContent();
        this.pageInfo = new PageInfo(page.getNumber() + 1, page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
