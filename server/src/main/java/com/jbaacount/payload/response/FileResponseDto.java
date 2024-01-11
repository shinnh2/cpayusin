package com.jbaacount.payload.response;

import com.jbaacount.model.File;
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
        this.storedFileName = file.getStoredFileName();
        this.url = file.getUrl();
    }
}
