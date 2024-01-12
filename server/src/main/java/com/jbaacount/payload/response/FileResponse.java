package com.jbaacount.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class FileResponse
{
    private String originalFileName;
    private String storedFileName;
    private String url;
}
