package com.TLU.SoundVerse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.TLU.SoundVerse.entity.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByTitleContainingIgnoreCase(String keyword);
    @Query(value = "SELECT * FROM album ORDER BY RAND() LIMIT 6", nativeQuery = true)
    List<Album> findRandomAlbums();   
}
