package com.jbaacount.board.dto.request;

import com.jbaacount.global.validation.notspace.NotSpace;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardPatchDto
{
    @NotNull
    private Long boardId;
    @NotSpace
    @Size(max = 15, message = "게시판 제목은 최대 15자까지 입력 가능합니다.")
    private String name;
    private Boolean isAdminOnly;
    private Long orderIndex;
    private Boolean isDeleted;
}
