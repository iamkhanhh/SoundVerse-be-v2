package com.TLU.SoundVerse.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.TLU.SoundVerse.entity.Album;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    List<Album> findByTitleContainingIgnoreCase(String keyword);
}
