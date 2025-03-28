package com.TLU.SoundVerse.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.TLU.SoundVerse.enums.UserRole;
import com.TLU.SoundVerse.repository.AlbumRepository;
import com.TLU.SoundVerse.repository.MusicRepository;
import com.TLU.SoundVerse.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminService {
    UserRepository userRepository;
    MusicRepository musicRepository;
    AlbumRepository albumRepository;

    public Map<String, Integer> getStatistics() {
       
        LocalDateTime startOfMonth = LocalDateTime.now().withDayOfMonth(1).toLocalDate().atStartOfDay();

        Map<String, Integer> stats = new HashMap<>();
        stats.put("totalUsers", userRepository.countUsers());
        stats.put("totalUsersMonthly", userRepository.countNewUsersThisMonth(startOfMonth));
        stats.put("totalArtists", userRepository.countArtists(UserRole.ARTIST));
        stats.put("totalSongs", musicRepository.countMusic());
        stats.put("totalSongsMonthly", musicRepository.countNewMusicThisMonth(startOfMonth));
        stats.put("totalAlbums", albumRepository.countAlbums());

        return stats;
    }

    

    
}
