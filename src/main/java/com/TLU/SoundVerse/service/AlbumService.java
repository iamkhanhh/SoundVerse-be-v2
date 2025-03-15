package com.TLU.SoundVerse.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.TLU.SoundVerse.dto.request.CreateAlbumDto;
import com.TLU.SoundVerse.dto.response.AlbumResponse;
import com.TLU.SoundVerse.dto.response.MusicResponse;
import com.TLU.SoundVerse.dto.response.UserResponse;
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
  MusicService musicService;

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

  public List<AlbumResponse> getAlbums() {
    List<Album> albums = albumRepository.findAll();

    return albums.stream().map(album -> 
            new AlbumResponse(
                album.getId(),
                album.getTitle(),
                album.getDescription(),
                album.getThumbnail(),
                
                album.getCreatedAt()
            )
        ).toList();
  }

}
