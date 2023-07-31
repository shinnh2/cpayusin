package com.jbaacount.board.dto.request;

import com.jbaacount.global.validation.notspace.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardPostDto
{
    @NotSpace
    private String name;

    private Boolean isAdminOnly;
}
