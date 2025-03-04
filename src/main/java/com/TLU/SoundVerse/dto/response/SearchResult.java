package com.TLU.SoundVerse.dto.response;

import java.util.List;

import com.TLU.SoundVerse.entity.Album;
import com.TLU.SoundVerse.entity.Artist;
import com.TLU.SoundVerse.entity.Music;

import lombok.Data;

@Data
public class SearchResult {
    private List<Artist> artist;
    private List<Music> music;
    private List<Album> album;

    public SearchResult(List<Artist> artists, List<Music> musics, List<Album> albums) {
        this.artist = artists;
        this.music = musics;
        this.album = albums;
    }

}
