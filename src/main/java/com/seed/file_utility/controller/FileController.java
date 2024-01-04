package com.seed.file_utility.controller;


import com.seed.file_utility.model.ValidFileType;
import com.seed.file_utility.model.File;
import com.seed.file_utility.service.FileTypeService;
import com.seed.file_utility.service.FileService;
import com.seed.file_utility.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
public class FileController {

    @Autowired
    private FileService fileService;

    @Autowired
    private MetadataService metadataService;

    @Autowired
    private FileTypeService fileTypeService;


    @PostMapping(path = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> upload(@RequestParam MultipartFile file) throws IOException {
        String message = "";

        if (file.isEmpty()) {
            message = "No file provided";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }

        String fileName = file.getOriginalFilename();
        int lastIndex = fileName.lastIndexOf('.') + 1;
        String fileType = fileName.substring(lastIndex).toUpperCase();

        if (fileTypeService.getAllowedFileTypes().contains(ValidFileType.valueOf(fileType))) {
            try {
                fileService.upload(file);
                message = "File upload successful";
                return ResponseEntity.status(HttpStatus.OK).body(message);
            } catch (Exception e) {
                message = "File upload failed";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
        }
        else {
            message = "File type - " + fileType + " not allowed.\nAllowed file types are - " + fileTypeService.getAllowedFileTypes();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }

    }

    @GetMapping("/download/{id}")
        public ResponseEntity<byte[]> download(@PathVariable long id) {
            Optional<File> optionalFile = fileService.download(id);
            if (optionalFile.isPresent()) {
                File file = optionalFile.get();
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                        .body(file.getData());
            }
            else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(("File with id " + id + " not found.").getBytes());
            }
        }


    @PostMapping(path = "/s3/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> s3Upload(@RequestParam MultipartFile file) throws IOException {
        String message = "";
        if (file.isEmpty()) {
            message = "No file provided";
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }

        String fileName = file.getOriginalFilename();
        int lastIndex = fileName.lastIndexOf('.') + 1;
        String fileType = fileName.substring(lastIndex).toUpperCase();

        if (fileTypeService.getAllowedFileTypes().contains(ValidFileType.valueOf(fileType))) {
            try {
                metadataService.upload(file);
                message = "File upload to S3 successful";
                return ResponseEntity.status(HttpStatus.OK).body(message);
            } catch (Exception e) {
                message = "File upload failed";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
            }
        }
        else {
            message = "File type - " + fileType + " not allowed.\nAllowed file types are - " + fileTypeService.getAllowedFileTypes();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
        }



    }

    @GetMapping("/s3/download/{fileName}")
    public HttpEntity<byte[]> s3Download(@PathVariable String fileName) throws IOException {

        return metadataService.download(fileName);
    }
}

