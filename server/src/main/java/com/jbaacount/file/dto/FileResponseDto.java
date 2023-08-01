package com.jbaacount.file.dto;

import com.jbaacount.file.entity.File;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FileResponseDto
{
    private String originalFileName;
    private String storedFileName;
    private String url;

    public FileResponseDto(File file)
    {
        this.originalFileName = file.getUploadFileName();
        this.storedFileName = file.getStoreFileName();
        this.url = file.getUrl();
    }
}
