package com.TLU.SoundVerse.service;

import org.springframework.stereotype.Service;

import com.TLU.SoundVerse.dto.response.SearchResult;
import com.TLU.SoundVerse.entity.Album;
import com.TLU.SoundVerse.entity.Artist;
import com.TLU.SoundVerse.entity.Music;
import com.TLU.SoundVerse.dto.response.ArtistResponse;
import com.TLU.SoundVerse.dto.response.MusicResponse;
import com.TLU.SoundVerse.dto.response.AlbumResponse;
import com.TLU.SoundVerse.repository.AlbumRepository;
import com.TLU.SoundVerse.repository.ArtistRepository;
import com.TLU.SoundVerse.repository.MusicRepository;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchService {
    ArtistRepository artistRepository;
    MusicRepository musicRepository;
    AlbumRepository albumRepository;
    CommonService commonService;
    MusicService musicService;
    AlbumService albumService;
    S3Service s3Service;

    public SearchResult search(String keyword) {
        List<Artist> artists = artistRepository.findArtistsByUserFullName(keyword);
        List<Music> musics = musicRepository.findByTitleContainingIgnoreCase(keyword);
        List<Album> albums = albumRepository.findByTitleContainingIgnoreCase(keyword);

        List<ArtistResponse> artistList = artists.stream().map(artist -> commonService.toArtistResponse(artist)).toList();
        List<MusicResponse> musicList = musics.stream().map(music -> musicService.toMusicResponse(music)).toList();
        List<AlbumResponse> albumList = albums.stream().map(album -> albumService.toAlbumResponse(album)).toList();

        return new SearchResult(artistList, musicList, albumList);
    }
}