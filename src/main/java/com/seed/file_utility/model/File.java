package com.seed.file_utility.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
@Table(name="files")
public class File {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    @Lob
    private byte[] data;

    public File(String name, byte[] data) {
        this.name = name;
        this.data = data;
    }
}
