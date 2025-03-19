package com.TLU.SoundVerse.dto.response;

import java.util.List;

import com.TLU.SoundVerse.dto.response.ArtistResponse;
import com.TLU.SoundVerse.dto.response.MusicResponse;
import com.TLU.SoundVerse.dto.response.AlbumResponse;

import lombok.Data;

@Data
public class SearchResult {
    private List<ArtistResponse> artist;
    private List<MusicResponse> music;
    private List<AlbumResponse> album;

    public SearchResult(List<ArtistResponse> artists, List<MusicResponse> musics, List<AlbumResponse> albums) {
        this.artist = artists;
        this.music = musics;
        this.album = albums;
    }

}
