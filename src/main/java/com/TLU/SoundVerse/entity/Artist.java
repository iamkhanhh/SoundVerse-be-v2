package com.TLU.SoundVerse.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
 
@Entity
@Data
public class Artist {
 @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer genreId;

    private Integer listOfMusic;

    private Integer albums;
}
