package com.jbaacount.config;

import com.amazonaws.services.s3.AmazonS3Client;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class MockS3Config extends S3Config
{
    @Bean
    @Primary
    @Override
    public AmazonS3Client amazonS3Client()
    {
        return Mockito.mock(S3Config.class).amazonS3Client();
    }
}
