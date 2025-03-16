package com.TLU.SoundVerse.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.TLU.SoundVerse.dto.request.CreateMusicDto;
import com.TLU.SoundVerse.dto.response.MusicResponse;
import com.TLU.SoundVerse.entity.Music;
import com.TLU.SoundVerse.enums.MusicStatus;
import com.TLU.SoundVerse.repository.MusicRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MusicService {
  MusicRepository musicRepository;
  S3Service s3Service;
  GenreService genreService;
  UserService userService;
  

  public Music createMusic(CreateMusicDto createMusicDto, Integer user_id) {
    Music newMusic = new Music();

    newMusic.setTitle(createMusicDto.getTitle());
    newMusic.setDescription(createMusicDto.getDescription());
    newMusic.setArtistId(user_id);
    newMusic.setThumbnail(user_id + "/thumbnails/" + createMusicDto.getThumbnail());
    newMusic.setAlbumsId(createMusicDto.getAlbumsId());
    newMusic.setStatus(MusicStatus.PENDING);
    newMusic.setGenreId(createMusicDto.getGenreId());
    newMusic.setLength(createMusicDto.getLength());
    newMusic.setFilePath(user_id + "/" + createMusicDto.getFilePath());

    musicRepository.save(newMusic);
    return newMusic;
  }

  public List<MusicResponse> getMusic(Integer userId) {
    List<Music> musicList = musicRepository.findByArtistId(userId);

    return musicList.stream()
                    .map(this::toMusicResponse)
                    .collect(Collectors.toList());
  }

  public List<MusicResponse> getMusicByAlbumId(Integer albumsId) {
    List<Music> musicList = musicRepository.findByAlbumsId(albumsId);
    
    return musicList.stream().map(this::toMusicResponse).collect(Collectors.toList());
  }

  public List<MusicResponse> getMusicsByPlaylsitId(Integer playlistId) {
    List<Music> musicList = musicRepository.findByPlaylistId(playlistId);
    
    return musicList.stream().map(this::toMusicResponse).collect(Collectors.toList());
  }

  public MusicResponse toMusicResponse(Music music) {
    Map<String, String> user = userService.getUserById(music.getArtistId());
    return MusicResponse.builder()
        .id(music.getId())
        .title(music.getTitle())
        .description(music.getDescription())
        .thumbnail(s3Service.getS3Url(music.getThumbnail()))
        .albumsId(music.getAlbumsId())
        .genre(genreService.getGenreById(music.getGenreId()))
        .artist(user.get("username"))
        .artistId(Integer.parseInt(user.get("id")))
        .length(music.getLength())
        .filePath(music.getFilePath())
        .createdAt(music.getCreatedAt())
        .build();
  }

  public List<MusicResponse> getAllMusic() {
    List<Music> musicList = musicRepository.findAll();

    return musicList.stream()
                    .map(this::toMusicResponse)
                    .collect(Collectors.toList());
}

}
