package com.seed.file_utility.controller;


import com.seed.file_utility.model.AllowedFileType;
import com.seed.file_utility.service.AllowedFileTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/allowed-file-types")
public class AllowedFileTypeController {

    @GetMapping
    public Set<AllowedFileType> getAllowedFileTypes() {
        return AllowedFileTypeService.getAllowedFileTypes();
    }

    @PostMapping("/add/{allowedFileType}")
    public ResponseEntity<String> addAllowedFileType(@PathVariable String allowedFileType) {
        allowedFileType = allowedFileType.toUpperCase();
        if (AllowedFileTypeService.addAllowedFileType(AllowedFileType.valueOf(allowedFileType))) {
            return ResponseEntity.status(HttpStatus.OK).body("File type - " + allowedFileType + " added to allowed file types.");
        }
        else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("File type - " + allowedFileType + " already exists.");
        }
    }

    @DeleteMapping("/remove/{allowedFileType}")
    public ResponseEntity<String> removeAllowedFileType(@PathVariable String allowedFileType) {
        allowedFileType = allowedFileType.toUpperCase();
        if (AllowedFileTypeService.removeAllowedFileType(AllowedFileType.valueOf(allowedFileType))) {
            return ResponseEntity.status(HttpStatus.OK).body("File type - " + allowedFileType + " removed from allowed file types.");
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File type - " + allowedFileType + " does not exist.");
        }
    }
}
