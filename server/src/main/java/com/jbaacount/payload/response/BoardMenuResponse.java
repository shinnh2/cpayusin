package com.jbaacount.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BoardMenuResponse
{
    private Long id;

    private String name;

    private String type;

    private Integer orderIndex;

    private List<BoardChildrenResponse> category = new ArrayList<>();
}
