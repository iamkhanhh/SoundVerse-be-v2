package com.TLU.SoundVerse.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MusicResponse {
  Integer id;

  String title;

  String description;

  String thumbnail;

  Integer albumsId;

  String genre;

  UserResponse artist;

  Float length;

  String filePath;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
  LocalDateTime createdAt;
}
