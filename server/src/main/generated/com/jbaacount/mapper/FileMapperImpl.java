package com.jbaacount.mapper;

import com.jbaacount.model.File;
import com.jbaacount.payload.response.FileResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-04-12T17:54:48+0900",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 19.0.2 (Amazon.com Inc.)"
)
@Component
public class FileMapperImpl implements FileMapper {

    @Override
    public FileResponse toFileResponse(File file) {
        if ( file == null ) {
            return null;
        }

        FileResponse fileResponse = new FileResponse();

        fileResponse.setUrl( file.getUrl() );

        return fileResponse;
    }

    @Override
    public List<FileResponse> toFileResponseList(List<File> fileList) {
        if ( fileList == null ) {
            return null;
        }

        List<FileResponse> list = new ArrayList<FileResponse>( fileList.size() );
        for ( File file : fileList ) {
            list.add( toFileResponse( file ) );
        }

        return list;
    }
}
