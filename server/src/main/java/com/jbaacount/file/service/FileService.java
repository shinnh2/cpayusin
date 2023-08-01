package com.jbaacount.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.jbaacount.file.entity.File;
import com.jbaacount.file.repository.FileRepository;
import com.jbaacount.global.exception.BusinessLogicException;
import com.jbaacount.global.exception.ExceptionMessage;
import com.jbaacount.post.entity.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class FileService
{
    private final FileRepository fileRepository;

    private final String bucket = "jbaccount";
    private final AmazonS3 amazonS3;

    public List<File> storeFiles(List<MultipartFile> files, Post post)
    {
        List<File> storedFiles = new ArrayList<>();

        for(MultipartFile file : files)
        {
            File storedFile = storeFile(file, post);
            storedFiles.add(storedFile);

            log.info("file saved successfully = {}", storedFile.getStoreFileName());
        }

        return storedFiles;
    }

    public void deleteUploadedFile(Post post)
    {
        List<File> files = fileRepository.findByPostId(post.getId());

        if(!files.isEmpty())
        {
            for (File file : files)
            {
                amazonS3.deleteObject(bucket, "post/" + file.getStoreFileName());
                log.info("file removed successfully = {}", file.getStoreFileName());
                post.removeFile(file);
            }
        }

        fileRepository.deleteAll(files);
    }


    private File storeFile(MultipartFile multipartFile, Post post)
    {
        String uploadFileName = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(uploadFileName);

        try{
            saveUploadFile(storeFileName, multipartFile);
        } catch (IOException e){
            throw new BusinessLogicException(ExceptionMessage.FILE_NOT_STORE);
        }

        File file = File.builder()
                .uploadFileName(uploadFileName)
                .storeFileName(storeFileName)
                .url(getFileUrl(storeFileName))
                .contentType(extractContentType(multipartFile))
                .build();

        file.addPost(post);
        return fileRepository.save(file);
    }

    private void saveUploadFile(String storeFileName, MultipartFile file) throws IOException
    {
        String contentType = extractContentType(file);
        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentType(contentType);
        metadata.setContentLength(file.getSize());
        amazonS3.putObject(bucket, "post/" + storeFileName, file.getInputStream(), metadata);
    }

    private String getFileUrl(String fileName)
    {
        return amazonS3.getUrl(bucket, "post/" + fileName).toString();
    }

    private String extractContentType(MultipartFile multipartFile)
    {
        String contentType = multipartFile.getContentType();

        return contentType;
    }

    private String createStoreFileName(String originalFileName)
    {
        String uuid = UUID.randomUUID().toString();

        String ext = extractedEXT(originalFileName);

        return uuid + "." + ext;
    }

    private String extractedEXT(String originalFileName)
    {
        int num = originalFileName.lastIndexOf(".");

        String ext = originalFileName.substring(num + 1);

        return ext;
    }
}
