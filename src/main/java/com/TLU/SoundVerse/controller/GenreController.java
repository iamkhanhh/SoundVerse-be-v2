package com.TLU.SoundVerse.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TLU.SoundVerse.dto.request.CreateGenreDto;
import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.entity.Genre;
import com.TLU.SoundVerse.service.GenreService;

import lombok.AccessLevel;

@RestController
@RequestMapping("genre")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenreController {
  GenreService genreService;

  @PostMapping
  ApiResponse<String> createGenre(@RequestBody CreateGenreDto request) {
    String genre_saved = genreService.createGenre(request.getGenre());

    ApiResponse<String> apiResponse = new ApiResponse<String>();
    apiResponse.setStatus("success");
    apiResponse.setMessage("Create new genre successfilly");
    apiResponse.setData(genre_saved);
    return apiResponse;
  }

  @GetMapping
  ApiResponse<List<Genre>> getGenres() {
    List<Genre> genre_saved = genreService.getGenres();

    ApiResponse<List<Genre>> apiResponse = new ApiResponse<List<Genre>>();
    apiResponse.setStatus("success");
    apiResponse.setMessage("Create new genre successfilly");
    apiResponse.setData(genre_saved);
    return apiResponse;
  }
}
