package com.jbaacount.payload.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardTypeResponse
{
    private Long id;

    private String name;

    public BoardTypeResponse(Long id, String name)
    {
        this.id = id;
        this.name = name;
    }
}
