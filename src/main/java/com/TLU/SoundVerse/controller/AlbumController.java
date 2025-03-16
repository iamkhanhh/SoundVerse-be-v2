package com.TLU.SoundVerse.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TLU.SoundVerse.dto.request.CreateAlbumDto;
import com.TLU.SoundVerse.dto.response.AlbumResponse;
import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.entity.Album;
import com.TLU.SoundVerse.service.AlbumService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("album")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AlbumController {
  AlbumService albumService;

  @PostMapping
  ApiResponse<Album> create(@RequestBody CreateAlbumDto createAlbumDto, HttpServletRequest request) {
    @SuppressWarnings("unchecked")
    Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
    Integer id = Integer.parseInt(String.valueOf(user.get("id")));
    Album albunm = albumService.create(createAlbumDto, id);

    ApiResponse<Album> apiResponse = new ApiResponse<Album>();
    apiResponse.setStatus("success");
    apiResponse.setMessage("Create new album successfilly");
    apiResponse.setData(albunm);
    return apiResponse;
  }

  @GetMapping
  ApiResponse<List<AlbumResponse>> getAlbums() {
    List<AlbumResponse> albums = albumService.getAlbums();

    ApiResponse<List<AlbumResponse>> apiResponse = new ApiResponse<List<AlbumResponse>>();
    apiResponse.setStatus("success");
    apiResponse.setMessage("Get album successfilly");
    apiResponse.setData(albums);
    return apiResponse;
  }

@GetMapping("/random")
ApiResponse<List<AlbumResponse>> getRandomAlbums() {
    List<AlbumResponse> albums = albumService.getRandomAlbums();
    return new ApiResponse<>(200, "Get random albums successfully", "success", albums);
}
}
