package com.TLU.SoundVerse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Music {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    private String description;

    private Integer artistId;

    private String thumbnail;

    private Integer albumsId;

    private Integer status;

    private Integer genreId;

    private Integer lenghth;

    private String filePath;
}
