package com.seed.file_utility.controller;


import com.seed.file_utility.model.ValidFileType;
import com.seed.file_utility.service.FileTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/allowed-file-types")
public class FileTypeController {

    @Autowired
    private FileTypeService fileTypeService;

    @GetMapping
    public Set<ValidFileType> getAllowedFileTypes() {
        return fileTypeService.getAllowedFileTypes();
    }

    @PostMapping("/add/{allowedFileType}")
    public ResponseEntity<String> addAllowedFileType(@PathVariable String allowedFileType) {
        return fileTypeService.addAllowedFileType(allowedFileType);
    }

    @DeleteMapping("/remove/{allowedFileType}")
    public ResponseEntity<String> removeAllowedFileType(@PathVariable String allowedFileType) {
        return fileTypeService.removeAllowedFileType(allowedFileType);
    }
}
