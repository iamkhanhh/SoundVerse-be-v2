package com.TLU.SoundVerse.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TLU.SoundVerse.dto.request.GetPresignedUrlForUploadDto;
import com.TLU.SoundVerse.dto.response.AlbumResponse;
import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.entity.Artist;
import com.TLU.SoundVerse.entity.Music;
import com.TLU.SoundVerse.service.CommonService;
import com.TLU.SoundVerse.service.S3Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommonController {
    S3Service s3Service;
    CommonService commonService;

  @PostMapping("/generate-single-presigned-url")
  ApiResponse<String> generateSinglePresignedUrl(HttpServletRequest request, @RequestBody GetPresignedUrlForUploadDto dto) {
    @SuppressWarnings("unchecked")
    Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
    Integer id = Integer.parseInt(String.valueOf(user.get("id")));
    String url = s3Service.createPresignedUrl(dto.getFileName(), id);

    ApiResponse<String> apiResponse = new ApiResponse<String>();
    apiResponse.setStatus("success");
    apiResponse.setMessage("Generate presigned url for upload successfilly");
    apiResponse.setData(url);
    return apiResponse;
  }

  @PostMapping("/generate-thumbnail-presigned-url")
  ApiResponse<String> createPresignedUrlForThumbnail(HttpServletRequest request, @RequestBody GetPresignedUrlForUploadDto dto) {
    @SuppressWarnings("unchecked")
    Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
    Integer id = Integer.parseInt(String.valueOf(user.get("id")));
    String url = s3Service.createPresignedUrlForThumbnail(dto.getFileName(), id);

    ApiResponse<String> apiResponse = new ApiResponse<String>();
    apiResponse.setStatus("success");
    apiResponse.setMessage("Generate presigned url for upload thumbnail successfilly");
    apiResponse.setData(url);
    return apiResponse;
  }

   
    @GetMapping("/random-albums")
    public ApiResponse<List<AlbumResponse>> getRandomAlbums() {
        List<AlbumResponse> albums = commonService.getRandomAlbums();
        return new ApiResponse<>(200, "Get random albums successfully", "success", albums);
    }

    
    @GetMapping("/top-followed-artists")
    public ApiResponse<List<Artist>> getTopFollowedArtists() {
        List<Artist> topArtists = commonService.getTopFollowedArtists();
        return new ApiResponse<>(200, "Get top followed artists successfully", "success", topArtists);
    }

    
    @GetMapping("/top-liked-music")
    public ApiResponse<List<Music>> getTopLikedMusic() {
        List<Music> topMusic = commonService.getTopLikedMusic();
        return new ApiResponse<>(200, "Get top liked music successfully", "success", topMusic);
    }

    
    @GetMapping("/random-music-by-followed-artists/{userId}")
    public ApiResponse<List<Music>> getRandomMusicByFollowedArtists(@PathVariable Integer userId) {
        List<Music> musicList = commonService.getRandomMusicByFollowedArtists(userId);
        return new ApiResponse<>(200, "Get random music by followed artists successfully", "success", musicList);
    }

}