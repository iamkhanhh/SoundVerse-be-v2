package com.TLU.SoundVerse.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.TLU.SoundVerse.dto.response.*;
import com.TLU.SoundVerse.entity.*;
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
    private final AlbumService albumService;
    private final MusicService musicService;
    private final UserService userService;

    public List<AlbumResponse> getRandomAlbums() {
        List<Album> albums = albumRepository.findRandomAlbums();
        return albums.stream().map(album -> albumService.toAlbumResponse(album)).toList();
    }

    public List<ArtistResponse> getTopFollowedArtists() {
        List<Object[]> topArtists = followerRepository.findTopFollowedArtists();

        List<Integer> topArtistIds = topArtists.stream()
                .map(obj -> (Integer) obj[0])
                .limit(4)
                .collect(Collectors.toList());

        List<Artist> artists = artistRepository.findAllById(topArtistIds);
        return artists.stream().map(artist -> toArtistResponse(artist)).toList();
    }

    public List<MusicResponse> getTopLikedMusic() {
        List<Object[]> topLikedMusic = likeRepository.findTopLikedMusic();
        List<Integer> topMusicIds = topLikedMusic.stream()
                .map(obj -> (Integer) obj[0])
                .limit(4)
                .collect(Collectors.toList());

        List<Music> musics =  musicRepository.findAllById(topMusicIds);

        return musics.stream().map(music -> musicService.toMusicResponse(music)).toList();
    }

    public List<MusicResponse> getRandomMusicByFollowedArtists(Integer userId) {
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
        List<Music> musics = musicList.stream().limit(6).collect(Collectors.toList());
        return musics.stream().map(music -> musicService.toMusicResponse(music)).toList();
    }

    public ArtistResponse toArtistResponse(Artist artist) {

        User user = userService.getUserById(artist.getUserId());

        List<MusicResponse> musics = musicService.getMusic(user.getId());

        List<AlbumResponse> albums  = albumService.getMusic(user.getId());

        return ArtistResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .gender(user.getGender())
                .country(user.getCountry())
                .profilePicImage(user.getProfilePicImage())
                .fullName(user.getFullName())
                .dob(user.getDob())
                .songs(musics)
                .albums(albums)
                .createdAt(artist.getCreatedAt())
                .build();
    }
}
