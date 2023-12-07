package com.seed.file_utility.service;


import com.seed.file_utility.model.File;
import com.seed.file_utility.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    public File upload(MultipartFile multipartFile) throws IOException {
        String name = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        File file = new File(name, multipartFile.getBytes());
        return fileRepository.save(file);
    }

    public File download(long id) {
        return fileRepository.findById(id).get();
    }

}
