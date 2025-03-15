package com.TLU.SoundVerse.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AlbumResponse {
  Integer id;

  String title;
  
  String description;

  String thumbnail;

  String artist;

  Integer artistId;

  Integer listOfMusic;

  List<MusicResponse> songs;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
  LocalDateTime createdAt;
}
