package com.TLU.SoundVerse.service;

import org.springframework.stereotype.Service;

import com.TLU.SoundVerse.dto.request.CreateAlbumDto;
import com.TLU.SoundVerse.entity.Album;
import com.TLU.SoundVerse.enums.MusicStatus;
import com.TLU.SoundVerse.repository.AlbumRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AlbumService {
  AlbumRepository albumRepository;

  public Album create(CreateAlbumDto createAlbumDto, Integer userId) {
    Album newAlbum = new Album();

    newAlbum.setArtistId(userId);
    newAlbum.setTitle(createAlbumDto.getTitle());
    newAlbum.setThumbnail(createAlbumDto.getThumbnail());
    newAlbum.setDescription(createAlbumDto.getDescription());
    newAlbum.setListOfMusic(0);
    newAlbum.setStatus(MusicStatus.PENDING);
    
    return albumRepository.save(newAlbum);
  }
}
