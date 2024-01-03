package com.seed.file_utility.service;


import com.seed.file_utility.model.AllowedFileType;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AllowedFileTypeService {
    private static final Set<AllowedFileType> allowedFileTypes = new HashSet<>();

    public static Set<AllowedFileType> getAllowedFileTypes() {
        return allowedFileTypes;
    }

    public static boolean addAllowedFileType(AllowedFileType allowedFileType) {
        return allowedFileTypes.add(allowedFileType);
    }

    public static boolean removeAllowedFileType(AllowedFileType allowedFileType) {
        return allowedFileTypes.remove(allowedFileType);
    }
}
