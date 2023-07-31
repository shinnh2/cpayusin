package com.jbaacount.board.dto.request;

import com.jbaacount.global.validation.notspace.NotSpace;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardPatchDto
{
    @NotSpace
    private String name;
    private Boolean isAdminOnly;
}
