package com.TLU.SoundVerse.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AlbumResponse {
  Integer id;

  String title;
  
  String description;

  String thumbnail;

  Integer listOfMusic;

  List<MusicResponse> songs;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
  LocalDateTime createdAt;
}
