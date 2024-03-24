package com.jbaacount.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse
{
    private Long id;

    private String name;

    private Integer orderIndex;

    private Long parentId;
}
