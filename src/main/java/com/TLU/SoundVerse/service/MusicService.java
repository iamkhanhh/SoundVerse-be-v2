package com.TLU.SoundVerse.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.TLU.SoundVerse.dto.request.CreateMusicDto;
import com.TLU.SoundVerse.dto.response.MusicResponse;
import com.TLU.SoundVerse.dto.response.UserResponse;
import com.TLU.SoundVerse.entity.Music;
import com.TLU.SoundVerse.enums.MusicStatus;
import com.TLU.SoundVerse.repository.FollowerRepository;
import com.TLU.SoundVerse.repository.MusicRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MusicService {
  MusicRepository musicRepository;
  FollowerRepository followerRepository;
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

  public MusicResponse toMusicResponse(Music music) {
    Map<String, String> user = userService.getUserById(music.getArtistId());
    return MusicResponse.builder()
        .id(music.getId())
        .title(music.getTitle())
        .description(music.getDescription())
        .thumbnail(music.getThumbnail())
        .albumsId(music.getAlbumsId())
        .genre(genreService.getGenreById(music.getGenreId()))
        .artist(user.get("username"))
        .artistId(Integer.parseInt(user.get("id")))
        .length(music.getLength())
        .filePath(music.getFilePath())
        .createdAt(music.getCreatedAt())
        .build();
  }

  public List<Music> getRandomMusicByFollowedArtists(Integer userId) {
  
    List<Integer> followedArtistIds = followerRepository.findByUserId(userId)
            .stream()
            .map(f -> f.getArtistId())
            .collect(Collectors.toList());

   
    List<Music> musicList = musicRepository.findAll()
            .stream()
            .filter(m -> followedArtistIds.contains(m.getArtistId()))
            .collect(Collectors.toList());

    if (musicList.size() < 6) {
        List<Music> allMusic = musicRepository.findAll();
        Collections.shuffle(allMusic);
        for (Music music : allMusic) {
            if (!musicList.contains(music)) {
                musicList.add(music);
            }
            if (musicList.size() >= 6) {
                break;
            }
        }
    }

    Collections.shuffle(musicList);
    return musicList.stream().limit(6).collect(Collectors.toList());
}
}
