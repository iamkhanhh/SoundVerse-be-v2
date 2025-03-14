package com.TLU.SoundVerse.service;

import com.TLU.SoundVerse.dto.request.PlaylistDto;
import com.TLU.SoundVerse.dto.request.CreateMusicDto;
import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.entity.MusicsOfPlaylist;
import com.TLU.SoundVerse.entity.Playlist;
import com.TLU.SoundVerse.entity.Music;
import com.TLU.SoundVerse.repository.MusicsOfPlaylistRepository;
import com.TLU.SoundVerse.repository.PlaylistRepository;
import com.TLU.SoundVerse.repository.MusicRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;
    private final MusicsOfPlaylistRepository musicsOfPlaylistRepository;
    private final MusicRepository musicRepository;

    public ApiResponse<List<PlaylistDto>> getUserPlaylists(HttpServletRequest request) {
        Integer userId = getUserIdFromRequest(request);

        if (userId == null) {
            return ApiResponse.<List<PlaylistDto>>builder()
                .code(400)
                .message("User ID not found")
                .status("failed")
                .data(null)
                .build();
        }

        List<Playlist> playlists = playlistRepository.findByUserIdAndIsDeleted(userId, 0);
        List<PlaylistDto> playlistDtos = playlists.stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());

        return ApiResponse.<List<PlaylistDto>>builder()
            .code(200)
            .message("Successfully fetched playlists")
            .status("success")
            .data(playlistDtos)
            .build();
    }

    public ApiResponse<PlaylistDto> createPlaylist(HttpServletRequest request, PlaylistDto dto) {
        Integer userId = getUserIdFromRequest(request);

        if (userId == null) {
            return ApiResponse.<PlaylistDto>builder()
                .code(400)
                .message("User ID not found")
                .status("failed")
                .build();
        }

        Playlist newPlaylist = Playlist.builder()
            .title(dto.getTitle())
            .description(dto.getDescription())
            .thumbnail(dto.getThumbnail())
            .userId(userId)
            .build();

        playlistRepository.save(newPlaylist);
        PlaylistDto responseDto = convertToDto(newPlaylist);

        return ApiResponse.<PlaylistDto>builder()
            .code(201)
            .message("Playlist created successfully")
            .status("success")
            .data(responseDto)
            .build();
    }

    public ApiResponse<String> deletePlaylist(HttpServletRequest request, Integer playlistId) {
        Integer userId = getUserIdFromRequest(request);

        if (userId == null) {
            return ApiResponse.<String>builder()
                .code(400)
                .message("User ID not found")
                .status("failed")
                .build();
        }

        Optional<Playlist> optionalPlaylist = playlistRepository.findById(playlistId);
        if (optionalPlaylist.isEmpty() || !optionalPlaylist.get().getUserId().equals(userId)) {
            return ApiResponse.<String>builder()
                .code(404)
                .message("Playlist not found or not owned by you")
                .status("failed")
                .build();
        }

        Playlist playlist = optionalPlaylist.get();
        playlist.setIsDeleted(1);
        playlistRepository.save(playlist);

        return ApiResponse.<String>builder()
            .code(200)
            .message("Playlist deleted successfully")
            .status("success")
            .build();
    }

    public ApiResponse<List<CreateMusicDto>> getMusicsInPlaylist(Integer playlistId) {
        List<MusicsOfPlaylist> musicsInPlaylist = musicsOfPlaylistRepository.findByAlbumsId(playlistId);
        List<Integer> musicIds = musicsInPlaylist.stream()
            .map(MusicsOfPlaylist::getMusicId)
            .toList();

        List<Music> musics = musicRepository.findAllById(musicIds);

        List<CreateMusicDto> musicDtos = musics.stream().map(music ->
            CreateMusicDto.builder()
                .title(music.getTitle())
                .description(music.getDescription())
                .thumbnail(music.getThumbnail())
                .albumsId(music.getAlbumsId())
                .genreId(music.getGenreId())
                .length(music.getLength())
                .filePath(music.getFilePath())
                .build()
        ).toList();

        return ApiResponse.<List<CreateMusicDto>>builder()
            .code(200)
            .message("Successfully fetched playlist songs")
            .status("success")
            .data(musicDtos)
            .build();
    }

    public ApiResponse<String> removeMusicFromPlaylist(Integer playlistId, Integer musicId) {
        musicsOfPlaylistRepository.deleteByAlbumsIdAndMusicId(playlistId, musicId);

        return ApiResponse.<String>builder()
            .code(200)
            .message("Successfully removed song from playlist")
            .status("success")
            .build();
    }

    public ApiResponse<String> addMusicToPlaylist(Integer playlistId, Integer musicId) {
        MusicsOfPlaylist newEntry = new MusicsOfPlaylist();
        newEntry.setAlbumsId(playlistId);
        newEntry.setMusicId(musicId);

        musicsOfPlaylistRepository.save(newEntry);

        return ApiResponse.<String>builder()
            .code(201)
            .message("Successfully added song to playlist")
            .status("success")
            .build();
    }

    private PlaylistDto convertToDto(Playlist playlist) {
        return PlaylistDto.builder()
            .title(playlist.getTitle())
            .description(playlist.getDescription())
            .thumbnail(playlist.getThumbnail())
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
