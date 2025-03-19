package com.TLU.SoundVerse.service;

import org.springframework.stereotype.Service;
import com.TLU.SoundVerse.entity.Artist;
import com.TLU.SoundVerse.repository.ArtistRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArtistService {
    ArtistRepository artistRepository;

    public Artist createArtist(Integer userId) {
        Artist artist = new Artist();

        artist.setUserId(userId);
        artist.setListOfMusic(0);
        artist.setAlbums(0);

        return artistRepository.save(artist);
    }

    public Integer getArtistIdByUserId(Integer userId) {
        Artist artist = artistRepository.findById(userId).orElseThrow(() -> new RuntimeException("Artist not found!"));
        return artist.getId();
    }
}
