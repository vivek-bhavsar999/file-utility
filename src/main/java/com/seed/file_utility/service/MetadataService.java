package com.seed.file_utility.service;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.seed.file_utility.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    public HttpEntity<byte[]> download(String key) throws IOException {
        try {
            S3Object s3Object = fileService.s3Download(bucketName, key);
            String contentType = s3Object.getObjectMetadata().getContentType();
            var bytes = s3Object.getObjectContent().readAllBytes();
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.valueOf(contentType));
            header.setContentLength(bytes.length);
            header.setContentDispositionFormData("attachment", key);
            return new ResponseEntity<>(bytes, header, HttpStatus.OK);
        }
        catch (AmazonS3Exception e) {
            return new ResponseEntity<>(e.getMessage().getBytes(StandardCharsets.UTF_8), HttpStatus.NOT_FOUND);
        }
    }
}
