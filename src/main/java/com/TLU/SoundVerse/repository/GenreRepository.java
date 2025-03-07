package com.TLU.SoundVerse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.TLU.SoundVerse.entity.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {
  boolean existsByTitle(String genre);
}
