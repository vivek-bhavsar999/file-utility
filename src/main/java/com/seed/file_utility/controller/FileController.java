package com.seed.file_utility.controller;


import com.amazonaws.services.s3.model.S3Object;
import com.seed.file_utility.model.File;
import com.seed.file_utility.service.FileService;
import com.seed.file_utility.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private MetadataService metadataService;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestParam MultipartFile file) throws IOException {
        String message = "";
        try {
            fileService.upload(file);
            message = "File upload successful";
            return ResponseEntity.status(HttpStatus.OK).body(message);
        } catch (Exception e) {
            message = "File upload failed";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }
    }

    @GetMapping("/download/{id}")
        public ResponseEntity<byte[]> download(@PathVariable long id) {
            File file = fileService.download(id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(file.getData());
        }


    @PostMapping("/s3/upload")
    public ResponseEntity<String> s3Upload(@RequestParam MultipartFile file) throws IOException {
        String message = "";

            metadataService.upload(file);
            message = "File upload to S3 successful";
            return ResponseEntity.status(HttpStatus.OK).body(message);

    }

    @GetMapping("/s3/download/{fileName}")
    public HttpEntity<byte[]> s3Download(@PathVariable String fileName) throws IOException {
        S3Object s3Object = metadataService.download(fileName);
        String contentType = s3Object.getObjectMetadata().getContentType();
        var bytes = s3Object.getObjectContent().readAllBytes();
        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.valueOf(contentType));
        header.setContentLength(bytes.length);
        header.setContentDispositionFormData("attachment", fileName);
        return new ResponseEntity<>(bytes, header, HttpStatus.OK);
    }
}

