package com.TLU.SoundVerse.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.TLU.SoundVerse.dto.request.CreateMusicDto;
import com.TLU.SoundVerse.dto.response.MusicResponse;
import com.TLU.SoundVerse.entity.Artist;
import com.TLU.SoundVerse.entity.Music;
import com.TLU.SoundVerse.enums.MusicStatus;
import com.TLU.SoundVerse.repository.ArtistRepository;
import com.TLU.SoundVerse.repository.MusicRepository;
import com.TLU.SoundVerse.repository.MusicsOfPlaylistRepository;
import com.TLU.SoundVerse.repository.UserRepository;

import jakarta.mail.MessagingException;

import com.TLU.SoundVerse.entity.MusicsOfPlaylist;
import com.TLU.SoundVerse.entity.User;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MusicService {
  MusicRepository musicRepository;
  S3Service s3Service;
  GenreService genreService;
  UserService userService;
  MusicsOfPlaylistRepository musicsOfPlaylistRepository;
  EmailService emailService;
  UserRepository userRepository;
  ArtistRepository artistRepository;

  public MusicResponse createMusic(CreateMusicDto createMusicDto, Integer user_id) {
    Integer artistId = userService.getArtistIdByUserId(user_id);
    Music newMusic = new Music();

    newMusic.setTitle(createMusicDto.getTitle());
    newMusic.setDescription(createMusicDto.getDescription());
    newMusic.setArtistId(artistId);
    newMusic.setThumbnail(user_id + "/thumbnails/" + createMusicDto.getThumbnail());
    newMusic.setAlbumsId(createMusicDto.getAlbumsId());
    newMusic.setStatus(MusicStatus.PENDING);
    newMusic.setGenreId(createMusicDto.getGenreId());
    newMusic.setLength(createMusicDto.getLength());
    newMusic.setFilePath(user_id + "/" + createMusicDto.getFilePath());

    return toMusicResponse(musicRepository.save(newMusic));
  }

  public List<MusicResponse> getMusic(Integer userId) {

    Integer artistId = userService.getArtistIdByUserId(userId);

    List<Music> musicList = musicRepository.findByArtistId(artistId);

    return musicList.stream()
                    .map(this::toMusicResponse)
                    .collect(Collectors.toList());
  }

  public List<MusicResponse> getMusicByAlbumId(Integer albumsId) {
    List<Music> musicList = musicRepository.findByAlbumsId(albumsId);
    
    return musicList.stream().map(this::toMusicResponse).collect(Collectors.toList());
  }

  public List<MusicResponse> getMusicsByPlaylistId(Integer playlistId) {
    List<MusicsOfPlaylist> musicList = musicsOfPlaylistRepository.findByAlbumsId(playlistId);

    List<Music> musics =  musicList.stream()
                                  .map(mop -> musicRepository.findById(mop.getMusicId()).orElse(null))
                                  .collect(Collectors.toList());
    
    return musics.stream().map(this::toMusicResponse).collect(Collectors.toList());
  }

  public MusicResponse toMusicResponse(Music music) {
    Map<String, String> user = userService.getUsernameAndIdByArtistId(music.getArtistId());
    return MusicResponse.builder()
        .id(music.getId())
        .title(music.getTitle())
        .description(music.getDescription())
        .thumbnail(music.getThumbnail() != null ? s3Service.getS3Url(music.getThumbnail()) : "/default_playlist_thumbnail.jpg")
        .albumsId(music.getAlbumsId())
        .genre(genreService.getGenreById(music.getGenreId()))
        .artist(user.get("username"))
        .artistId(Integer.parseInt(user.get("id")))
        .length(music.getLength())
        .filePath(s3Service.getS3Url(music.getFilePath()))
        .createdAt(music.getCreatedAt())
        .build();
  }

  public List<MusicResponse> getAllMusic() {
    List<Music> musicList = musicRepository.findAll();

    return musicList.stream()
                    .map(this::toMusicResponse)
                    .collect(Collectors.toList());
}

public List<MusicResponse> getPendingMusic() {
    List<Music> musicList = musicRepository.findByStatus(MusicStatus.PENDING);

    return musicList.stream()
                    .map(this::toMusicResponse)
                    .collect(Collectors.toList());
}

  public List<MusicResponse> getUnpublishMusicByArtistId(Integer userId) {
      Integer artistId = userService.getArtistIdByUserId(userId);
      List<Music> musicList = musicRepository.findByArtistIdAndStatus(artistId, MusicStatus.UNPUBLISHED);
      
      return musicList.stream().map(this::toMusicResponse).collect(Collectors.toList());
  }

  private String getArtistEmail(Integer artistId) {
        return userRepository.findById(artistId)
                .map(User::getEmail)
                .orElseThrow(() -> new RuntimeException("Artist Not Found"));
    }

    public MusicResponse publishMusic(Integer musicId) throws MessagingException {
        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new RuntimeException("Music Not Found"));

        music.setStatus(MusicStatus.PUBLISHED);
        music.setUpdatedAt(LocalDateTime.now());
        musicRepository.save(music);

        String email = getArtistEmail(music.getArtistId());
        String subject = "🎉 Bài hát đã được publish!";
        String content = "<h3>Chúc mừng!</h3>"
                       + "<p>Bài hát <b>'" + music.getTitle() + "'</b> đã sẵn sàng để phát hành trên SoundVerse! 🚀</p>";

        emailService.sendEmail(email, subject, content);
        return toMusicResponse(music);
    }

    public MusicResponse refuseMusic(Integer musicId) throws MessagingException {
        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new RuntimeException("Music Not Found"));

        music.setStatus(MusicStatus.REFUSED);
        music.setUpdatedAt(LocalDateTime.now());
        musicRepository.save(music);

        String email = getArtistEmail(music.getArtistId());
        String subject = "❌ Bài hát bị từ chối publish!";
        String content = "<h3>Bài hát của bạn đã bị từ chối</h3>"
                       + "<p>Bài hát <b>'" + music.getTitle() + "'</b> đã bị từ chối publish.</p>"
                       + "<p>Vui lòng liên hệ với SoundVerse qua email <b>support@soundverse.com</b> để biết thêm chi tiết.</p>";

        emailService.sendEmail(email, subject, content);
        return toMusicResponse(music);
    }

    public MusicResponse approveMusic(Integer musicId) throws MessagingException {
        Music music = musicRepository.findById(musicId)
                .orElseThrow(() -> new RuntimeException("Music Not Found"));

        music.setStatus(MusicStatus.UNPUBLISHED);
        music.setUpdatedAt(LocalDateTime.now());
        musicRepository.save(music);

        String email = getArtistEmail(music.getArtistId());
        String subject = "✅ Bài hát đã sẵn sàng để publish!";
        String content = "<h3>Bài hát của bạn đã được phê duyệt</h3>"
                       + "<p>Bài hát <b>'" + music.getTitle() + "'</b> đã được phê duyệt.</p>"
                       + "<p>Bạn có thể publish khi sẵn sàng!</p>";

        emailService.sendEmail(email, subject, content);
        return toMusicResponse(music);
    }

    public List<MusicResponse> getPublishedMusicByArtistId(Integer userId) {
      Integer artistId = userService.getArtistIdByUserId(userId);
      List<Music> musicList = musicRepository.findByArtistIdAndStatus(artistId, MusicStatus.PUBLISHED);
      
      return musicList.stream().map(this::toMusicResponse).collect(Collectors.toList());
  }
  
  public List<MusicResponse> getPendingMusicByArtistId(Integer userId) {
      Integer artistId = userService.getArtistIdByUserId(userId);
      List<Music> musicList = musicRepository.findByArtistIdAndStatus(artistId, MusicStatus.PENDING);
      
      return musicList.stream().map(this::toMusicResponse).collect(Collectors.toList());
  }
  
  

    public Integer getArtistIdByUserId(Integer userId) {

        Artist artist = artistRepository.findByUserId(userId);

        return artist.getId();
    }

  

  

}
