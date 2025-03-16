package com.TLU.SoundVerse.service;

import com.TLU.SoundVerse.entity.Artist;
import com.TLU.SoundVerse.repository.ArtistRepository;
import com.TLU.SoundVerse.repository.FollowerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtistService {
    ArtistRepository artistRepository;
    FollowerRepository followerRepository;

    public List<Artist> getTopFollowedArtists() {
        List<Object[]> topArtists = followerRepository.findTopFollowedArtists();

        List<Integer> topArtistIds = topArtists.stream()
                .map(obj -> (Integer) obj[0])
                .limit(6)
                .collect(Collectors.toList());

        return artistRepository.findAllById(topArtistIds);
    }
}
