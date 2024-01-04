package com.seed.file_utility.service;


import com.seed.file_utility.model.ValidFileType;
import com.seed.file_utility.model.FileType;
import com.seed.file_utility.repository.FileTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FileTypeService {

    @Autowired
    private FileTypeRepository fileTypeRepository;

    public Set<ValidFileType> getAllowedFileTypes() {
        return fileTypeRepository.findAll().stream().map(entity -> ValidFileType.valueOf(entity.getFileType())).collect(Collectors.toSet());

    }

    public ResponseEntity<String> addAllowedFileType(String allowedFileType) {
        allowedFileType = allowedFileType.toUpperCase();
        for (ValidFileType validFileType1 : ValidFileType.values()) {
            if (validFileType1.name().equals(allowedFileType)) {
                if (!getAllowedFileTypes().contains(ValidFileType.valueOf(allowedFileType))) {
                    fileTypeRepository.save(new FileType(allowedFileType));
                    return ResponseEntity.status(HttpStatus.OK).body("File type - " + allowedFileType + " added to allowed file types.");
                }
                else {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("File type - " + allowedFileType + " already exists.");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File type - " + allowedFileType + " is invalid.");

    }

    public ResponseEntity<String> removeAllowedFileType(String allowedFileType) {
        allowedFileType = allowedFileType.toUpperCase();
        for (ValidFileType validFileType1 : ValidFileType.values()) {
            if (validFileType1.name().equals(allowedFileType)) {
                if (getAllowedFileTypes().contains(ValidFileType.valueOf(allowedFileType))) {
                    FileType fileTypeToDelete = fileTypeRepository.findByFileType(allowedFileType);
                    fileTypeRepository.delete(fileTypeToDelete);
                    return ResponseEntity.status(HttpStatus.OK).body("File type - " + allowedFileType + " removed from allowed file types.");
                }
                else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File type - " + allowedFileType + " does not exist.");
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("File type - " + allowedFileType + " is invalid.");
    }
}
