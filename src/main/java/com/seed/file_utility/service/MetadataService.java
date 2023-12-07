package com.seed.file_utility.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.seed.file_utility.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class MetadataService {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private AmazonS3Client amazonS3Client;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    public void upload(MultipartFile file) throws IOException {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        String path = bucketName;
        String fileName = String.format("%s", file.getOriginalFilename());

        PutObjectResult putObjectResult = fileService.s3Upload(path, fileName, Optional.of(metadata), file.getInputStream());
        System.out.println(putObjectResult.toString());
    }

    public S3Object download(String key) {
        return fileService.s3Download("/", key);
    }
}
