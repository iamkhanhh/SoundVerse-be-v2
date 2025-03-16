package com.TLU.SoundVerse.service;

import java.util.List;


import org.springframework.stereotype.Service;

import com.TLU.SoundVerse.entity.Genre;
import com.TLU.SoundVerse.repository.GenreRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenreService {
  GenreRepository genreRepository;

  public String createGenre(String genre) {
    if (genreRepository.existsByTitle(genre)) {
      throw new RuntimeException("This genre already exists");
    }

    Genre new_genre = new Genre();
    new_genre.setTitle(genre);

    genreRepository.save(new_genre);
    return new_genre.getTitle();
  }

  public List<Genre> getGenres() {
    List<Genre> genres = genreRepository.findAll();
    return genres;
  }

  public String getGenreById(Integer genreId) {
    Genre genre = genreRepository.findById(genreId)
                    .orElseThrow(() -> new RuntimeException("Genre not found!"));
    return genre.getTitle();
  }
}
