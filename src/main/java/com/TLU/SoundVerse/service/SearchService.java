package com.TLU.SoundVerse.service;

import org.springframework.stereotype.Service;

import com.TLU.SoundVerse.dto.response.SearchResult;
import com.TLU.SoundVerse.entity.Album;
import com.TLU.SoundVerse.entity.Artist;
import com.TLU.SoundVerse.entity.Music;
import com.TLU.SoundVerse.repository.AlbumRepository;
import com.TLU.SoundVerse.repository.ArtistRepository;
import com.TLU.SoundVerse.repository.MusicRepository;

import java.util.List;

@Service
public class SearchService {

    private final ArtistRepository artistRepository;
    private final MusicRepository musicRepository;
    private final AlbumRepository albumRepository;

    public SearchService(ArtistRepository artistRepository, MusicRepository musicRepository, AlbumRepository albumRepository) {
        this.artistRepository = artistRepository;
        this.musicRepository = musicRepository;
        this.albumRepository = albumRepository;
    }

    public SearchResult search(String keyword) {
        List<Artist> artists = artistRepository.findArtistsByUserFullName(keyword);
        List<Music> musics = musicRepository.findByTitleContainingIgnoreCase(keyword);
        List<Album> albums = albumRepository.findByTitleContainingIgnoreCase(keyword);

        return new SearchResult(artists, musics, albums);
    }
}