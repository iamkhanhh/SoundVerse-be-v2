package com.TLU.SoundVerse.service;

import com.TLU.SoundVerse.dto.request.MusicOfPlaylistDto;
import com.TLU.SoundVerse.dto.request.PlaylistDto;
import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.entity.MusicsOfPlaylist;
import com.TLU.SoundVerse.entity.Playlist;
import com.TLU.SoundVerse.repository.MusicsOfPlaylistRepository;
import com.TLU.SoundVerse.repository.PlaylistRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final MusicsOfPlaylistRepository musicsOfPlaylistRepository;

    
    public ApiResponse<List<Playlist>> getUserPlaylists(HttpServletRequest request) {
        Integer userId = getUserIdFromRequest(request);

        if (userId == null) {
            return ApiResponse.<List<Playlist>>builder()
                .code(400)
                .message("Không tìm thấy User ID")
                .status("FAILED")
                .data(null)
                .build();
        }

        List<Playlist> playlists = playlistRepository.findByUserIdAndIsDeleted(userId, 0);
        return ApiResponse.<List<Playlist>>builder()
            .code(200)
            .message("Lấy danh sách Playlist thành công")
            .status("SUCCESS")
            .data(playlists)
            .build();
    }

  
    public ApiResponse<Playlist> createPlaylist(HttpServletRequest request, PlaylistDto dto) {
        Integer userId = getUserIdFromRequest(request);

        if (userId == null) {
            return ApiResponse.<Playlist>builder()
                .code(400)
                .message("Không tìm thấy User ID")
                .status("FAILED")
                .build();
        }

        Playlist newPlaylist = new Playlist();
        newPlaylist.setTitle(dto.getTitle());
        newPlaylist.setDescription(dto.getDescription());
        newPlaylist.setUserId(userId);
        newPlaylist.setThumbnail(dto.getThumbnail());
        newPlaylist.setIsDeleted(0);

        playlistRepository.save(newPlaylist);

        return ApiResponse.<Playlist>builder()
            .code(201)
            .message("Tạo Playlist thành công")
            .status("SUCCESS")
            .data(newPlaylist)
            .build();
    }


    public ApiResponse<String> deletePlaylist(HttpServletRequest request, Integer playlistId) {
        Integer userId = getUserIdFromRequest(request);

        if (userId == null) {
            return ApiResponse.<String>builder()
                .code(400)
                .message("Không tìm thấy User ID")
                .status("FAILED")
                .build();
        }

        Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);
        if (optionalPlaylist.isEmpty() || !optionalPlaylist.get().getUserId().equals(userId)) {
            return ApiResponse.<String>builder()
                .code(404)
                .message("Playlist không tồn tại hoặc không thuộc về bạn")
                .status("FAILED")
                .build();
        }

        Playlist playlist = optionalPlaylist.get();
        playlist.setIsDeleted(1); 
        playlistRepository.save(playlist);

        return ApiResponse.<String>builder()
            .code(200)
            .message("Xoá Playlist thành công")
            .status("SUCCESS")
            .build();
    }
   
    public ApiResponse<List<MusicOfPlaylistDto>> getMusicsInPlaylist(Integer playlistId) {
        List<MusicsOfPlaylist> musics = musicsOfPlaylistRepository.findByAlbumsId(playlistId);

        List<MusicOfPlaylistDto> musicDtos = musics.stream().map(music -> {
            MusicOfPlaylistDto dto = new MusicOfPlaylistDto();
            dto.setAlbumsId(music.getAlbumsId());
            dto.setMusicId(music.getMusicId());
            return dto;
        }).toList();

        return ApiResponse.<List<MusicOfPlaylistDto>>builder()
            .code(200)
            .message("Lấy danh sách bài hát thành công")
            .status("SUCCESS")
            .data(musicDtos)
            .build();
    }

    public ApiResponse<String> removeMusicFromPlaylist(MusicOfPlaylistDto dto) {
        musicsOfPlaylistRepository.deleteByAlbumsIdAndMusicId(dto.getAlbumsId(), dto.getMusicId());

        return ApiResponse.<String>builder()
            .code(200)
            .message("Xoá bài hát khỏi Playlist thành công")
            .status("SUCCESS")
            .build();
    }

    public ApiResponse<String> addMusicToPlaylist(MusicOfPlaylistDto dto) {
        MusicsOfPlaylist newEntry = new MusicsOfPlaylist();
        newEntry.setAlbumsId(dto.getAlbumsId());
        newEntry.setMusicId(dto.getMusicId());

        musicsOfPlaylistRepository.save(newEntry);

        return ApiResponse.<String>builder()
            .code(201)
            .message("Thêm bài hát vào Playlist thành công")
            .status("SUCCESS")
            .build();
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
