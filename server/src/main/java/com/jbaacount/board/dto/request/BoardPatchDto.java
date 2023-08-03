package com.jbaacount.board.dto.request;

import com.jbaacount.global.validation.notspace.NotSpace;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardPatchDto
{
    @NotSpace
    @Max(value = 15, message = "게시판 제목은 최대 15자까지 입력 가능합니다.")
    private String name;
    private Boolean isAdminOnly;
}
