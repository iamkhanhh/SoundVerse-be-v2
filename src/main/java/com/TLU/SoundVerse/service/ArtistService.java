package com.TLU.SoundVerse.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.TLU.SoundVerse.entity.Artist;
import com.TLU.SoundVerse.repository.AlbumRepository;
import com.TLU.SoundVerse.repository.ArtistRepository;
import com.TLU.SoundVerse.repository.FollowerRepository;
import com.TLU.SoundVerse.repository.MusicRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArtistService {
    ArtistRepository artistRepository;
    MusicRepository musicRepository;
    AlbumRepository albumRepository;
    FollowerRepository followerRepository;

    public Artist createArtist(Integer userId) {
        Artist artist = new Artist();

        artist.setUserId(userId);
        artist.setListOfMusic(0);
        artist.setAlbums(0);

        return artistRepository.save(artist);
    }

    // public Integer getArtistIdByUserId(Integer userId) {
    //     Artist artist = artistRepository.findById(userId).orElseThrow(() -> new RuntimeException("Artist not found!"));
    //     return artist.getId();
    // }

    public Integer getArtistIdByUserId(Integer userId) {

        Artist artist = artistRepository.findByUserId(userId);

        return artist.getId();
    }

    public Map<String, Integer> getArtistStats(HttpServletRequest request) {
        Integer userId = getUserIdFromRequest(request);
        System.out.println("kien" + userId);
        Integer artistId = getArtistIdByUserId(userId);
        System.out.println("kien" + artistId);
        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalSongs", musicRepository.countMusicByArtist(artistId));
        stats.put("totalAlbums", albumRepository.countAlbumsByArtist(artistId));
        stats.put("totalFollowers", followerRepository.countFollowersByArtist(artistId));
        return stats;
    }

    private Integer getUserIdFromRequest(HttpServletRequest request) {
        Object userObj = request.getAttribute("user");

        if (userObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> user = (Map<String, Object>) userObj;

            Object idObj = user.get("id");
            if (idObj != null) {
                try {
                    return Integer.parseInt(idObj.toString());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }

}
