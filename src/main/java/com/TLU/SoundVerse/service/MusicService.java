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
import com.TLU.SoundVerse.repository.MusicsOfPlaylistRepository;
import com.TLU.SoundVerse.entity.MusicsOfPlaylist;

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
  MusicsOfPlaylistRepository musicsOfPlaylistRepository;
  

  public MusicResponse createMusic(CreateMusicDto createMusicDto, Integer user_id) {
    Integer artistId = userService.getArtistIdByUserId(user_id);
    Music newMusic = new Music();

    newMusic.setTitle(createMusicDto.getTitle());
    newMusic.setDescription(createMusicDto.getDescription());
    newMusic.setArtistId(artistId);
    newMusic.setThumbnail(user_id + "/thumbnails/" + createMusicDto.getThumbnail());
    newMusic.setAlbumsId(createMusicDto.getAlbumsId());
    newMusic.setStatus(MusicStatus.PENDING);
    newMusic.setGenreId(createMusicDto.getGenreId());
    newMusic.setLength(createMusicDto.getLength());
    newMusic.setFilePath(user_id + "/" + createMusicDto.getFilePath());

    return toMusicResponse(musicRepository.save(newMusic));
  }

  public List<MusicResponse> getMusic(Integer userId) {

    Integer artistId = userService.getArtistIdByUserId(userId);

    List<Music> musicList = musicRepository.findByArtistId(artistId);

    return musicList.stream()
                    .map(this::toMusicResponse)
                    .collect(Collectors.toList());
  }

  public List<MusicResponse> getMusicByAlbumId(Integer albumsId) {
    List<Music> musicList = musicRepository.findByAlbumsId(albumsId);
    
    return musicList.stream().map(this::toMusicResponse).collect(Collectors.toList());
  }

  public List<MusicResponse> getMusicsByPlaylistId(Integer playlistId) {
    List<MusicsOfPlaylist> musicList = musicsOfPlaylistRepository.findByAlbumsId(playlistId);

    List<Music> musics =  musicList.stream()
                                  .map(mop -> musicRepository.findById(mop.getMusicId()).orElse(null))
                                  .collect(Collectors.toList());
    
    return musics.stream().map(this::toMusicResponse).collect(Collectors.toList());
  }

  public MusicResponse toMusicResponse(Music music) {
    Map<String, String> user = userService.getUsernameAndIdByArtistId(music.getArtistId());
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
        .filePath(s3Service.getS3Url(music.getFilePath()))
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
