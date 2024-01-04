package com.seed.file_utility.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name="filetypes")
public class FileType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String fileType;

    public FileType(String fileType) {
        this.fileType = fileType;
    }
}
