package com.seed.file_utility.repository;


import com.seed.file_utility.model.FileType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileTypeRepository extends JpaRepository<FileType, Long> {

    FileType findByFileType(String fileType);
}
