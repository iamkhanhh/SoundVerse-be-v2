package com.TLU.SoundVerse.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.TLU.SoundVerse.dto.response.AlbumResponse;
import com.TLU.SoundVerse.entity.Album;
import com.TLU.SoundVerse.entity.Artist;
import com.TLU.SoundVerse.entity.Music;
import com.TLU.SoundVerse.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommonService {
    
    private final AlbumRepository albumRepository;
    private final FollowerRepository followerRepository;
    private final ArtistRepository artistRepository;
    private final LikeRepository likeRepository;
    private final MusicRepository musicRepository;


    public List<AlbumResponse> getRandomAlbums() {
        List<Album> albums = albumRepository.findRandomAlbums();
        return albums.stream().map(this::toAlbumResponse).toList();
    }

    private AlbumResponse toAlbumResponse(Album album) {
        return AlbumResponse.builder()
            .id(album.getId())
            .title(album.getTitle())
            .description(album.getDescription())
            .thumbnail(album.getThumbnail())
            .artistId(album.getArtistId())
            .createdAt(album.getCreatedAt())
            .build();
    }


    public List<Artist> getTopFollowedArtists() {
        List<Object[]> topArtists = followerRepository.findTopFollowedArtists();

        List<Integer> topArtistIds = topArtists.stream()
                .map(obj -> (Integer) obj[0])
                .limit(4)
                .collect(Collectors.toList());

        return artistRepository.findAllById(topArtistIds);
    }


    public List<Music> getTopLikedMusic() {
        List<Object[]> topLikedMusic = likeRepository.findTopLikedMusic();
        List<Integer> topMusicIds = topLikedMusic.stream()
                .map(obj -> (Integer) obj[0])
                .limit(4)
                .collect(Collectors.toList());
      
        return musicRepository.findAllById(topMusicIds);
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
            List<Music> additionalMusic = musicRepository.findAll();
            Collections.shuffle(additionalMusic);

            for (Music music : additionalMusic) {
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
