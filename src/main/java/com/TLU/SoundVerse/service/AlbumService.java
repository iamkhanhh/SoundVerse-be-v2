package com.TLU.SoundVerse.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.TLU.SoundVerse.dto.request.CreateAlbumDto;
import com.TLU.SoundVerse.dto.response.AlbumResponse;
import com.TLU.SoundVerse.dto.response.MusicResponse;
import com.TLU.SoundVerse.dto.response.UserResponse;
import com.TLU.SoundVerse.entity.Album;
import com.TLU.SoundVerse.entity.Music;
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
  UserService userService;
  S3Service s3Service;

  public Album create(CreateAlbumDto createAlbumDto, Integer userId) {
    Integer artistId = userService.getArtistIdByUserId(userId);
    Album newAlbum = new Album();

    newAlbum.setArtistId(artistId);
    newAlbum.setTitle(createAlbumDto.getTitle());
    newAlbum.setThumbnail(createAlbumDto.getThumbnail());
    newAlbum.setDescription(createAlbumDto.getDescription());
    newAlbum.setListOfMusic(0);
    newAlbum.setStatus(MusicStatus.PENDING);

    return albumRepository.save(newAlbum);
  }

  public List<AlbumResponse> getAlbums() {
    List<Album> albums = albumRepository.findAll();

    return albums.stream().map(album -> toAlbumResponse(album)).toList();
  }

  public List<AlbumResponse> getMusic(Integer userId) {

    Integer artistId = userService.getArtistIdByUserId(userId);

    List<Album> musicList = albumRepository.findByArtistId(artistId);

    return musicList.stream()
                    .map(this::toAlbumResponse)
                    .collect(Collectors.toList());
  }

  public AlbumResponse toAlbumResponse(Album album) {

    Map<String, String> user = userService.getUsernameAndIdById(album.getArtistId());

    return AlbumResponse.builder()
        .id(album.getId())
        .title(album.getTitle())
        .description(album.getDescription())
        .thumbnail(s3Service.getS3Url(album.getThumbnail()))
        .artist(user.get("username"))
        .artistId(Integer.parseInt(user.get("id")))
        .listOfMusic(album.getListOfMusic())
        .songs(musicService.getMusicByAlbumId(album.getId()))
        .createdAt(album.getCreatedAt())
        .build();
  }

}
