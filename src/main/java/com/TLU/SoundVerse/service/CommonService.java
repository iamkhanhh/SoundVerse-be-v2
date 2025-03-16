package com.TLU.SoundVerse.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.TLU.SoundVerse.repository.AlbumRepository;
import com.TLU.SoundVerse.repository.MusicRepository;
import com.TLU.SoundVerse.repository.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommonService {
    UserRepository userRepository;
    MusicRepository musicRepository;
    AlbumRepository albumRepository;

    public Map<String, Integer> getStatistics() {
       
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).toLocalDate().atStartOfDay();

        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.countUsers());
        stats.put("newUsersThisMonth", userRepository.countNewUsersThisMonth(startOfMonth));
        stats.put("totalArtists", userRepository.countArtists());
        stats.put("totalMusic", musicRepository.countMusic());
        stats.put("newMusicThisMonth", musicRepository.countNewMusicThisMonth(startOfMonth));
        stats.put("totalAlbums", albumRepository.countAlbums());

        return stats;
    }
}
