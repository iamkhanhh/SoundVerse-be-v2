package com.TLU.SoundVerse.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateMusicDto {
  String title;

  String description;

  Integer artistId;

  String thumbnail;

  Integer albumsId;

  Integer genreId;

  Integer length;
}
