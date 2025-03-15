package com.TLU.SoundVerse.entity;
 
import java.time.LocalDateTime;
 
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
 
import com.TLU.SoundVerse.enums.MusicStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
 
@Builder
@Entity
@Data
public class Playlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String title;
 
    private String description;
 
    private Integer userId;

    private String thumbnail;
 
    private Integer listOfMusic;
 
    private MusicStatus status;
 
    private Integer isDeleted;

    @CreationTimestamp
    private LocalDateTime createdAt;
 
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}