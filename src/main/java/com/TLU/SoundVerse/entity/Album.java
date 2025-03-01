package com.TLU.SoundVerse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String title;

    private String description;

    private Integer artistId;

    private Integer isUserPlaylist;
    
    private String thumbnail;

    private Integer listOfMusic;

    private Integer status;

}
