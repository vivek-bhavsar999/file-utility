package com.seed.file_utility.service;


import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.seed.file_utility.model.File;
import com.seed.file_utility.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FileService {

    @Autowired
    private AmazonS3 amazonS3;

    @Autowired
    private FileRepository fileRepository;

    @Value("${aws.s3.bucket.name}")
    private String bucketName;

    public File upload(MultipartFile multipartFile) throws IOException {
        String name = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        File file = new File(name, multipartFile.getBytes());
        return fileRepository.save(file);
    }

    public File download(long id) {
        return fileRepository.findById(id).get();
    }

    public PutObjectResult s3Upload(String path, String fileName, Optional<Map<String, String>> optionalMetadata, InputStream inputStream) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        optionalMetadata.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(objectMetadata::addUserMetadata);
            }
        });
        PutObjectResult putObjectResult = amazonS3.putObject(path, fileName, inputStream, objectMetadata);
        return putObjectResult;
    }

    public S3Object s3Download(String path, String fileName) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, fileName);
        return amazonS3.getObject(getObjectRequest);
    }
}
