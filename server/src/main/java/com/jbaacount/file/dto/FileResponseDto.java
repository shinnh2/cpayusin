package com.jbaacount.file.dto;

import com.jbaacount.file.entity.File;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FileResponseDto
{
    private String originalFileName;
    private String storedfileName;
    private String url;

    public FileResponseDto(File file)
    {
        this.originalFileName = file.getUploadFileName();
        this.storedfileName = file.getStoreFileName();
        this.url = file.getUrl();
    }
}
