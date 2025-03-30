package com.TLU.SoundVerse.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TLU.SoundVerse.dto.request.CreateAlbumDto;
import com.TLU.SoundVerse.dto.response.AlbumResponse;
import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.service.AlbumService;
import org.springframework.web.bind.annotation.PathVariable;

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
  ApiResponse<AlbumResponse> create(@RequestBody CreateAlbumDto createAlbumDto, HttpServletRequest request) {
    @SuppressWarnings("unchecked")
    Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
    Integer id = Integer.parseInt(String.valueOf(user.get("id")));
    AlbumResponse albunm = albumService.create(createAlbumDto, id);

    ApiResponse<AlbumResponse> apiResponse = new ApiResponse<AlbumResponse>();
    apiResponse.setStatus("success");
    apiResponse.setMessage("Create new album successfilly");
    apiResponse.setData(albunm);
    return apiResponse;
  }

  @GetMapping("/{albumId}")
  ApiResponse<AlbumResponse> getAlbumById(@PathVariable Integer albumId) {
    AlbumResponse albums = albumService.getAlbumById(albumId);

    ApiResponse<AlbumResponse> apiResponse = new ApiResponse<AlbumResponse>();
    apiResponse.setStatus("success");
    apiResponse.setMessage("Get album successfilly");
    apiResponse.setData(albums);
    return apiResponse;
  }

  @DeleteMapping("/{albumId}")
  public ApiResponse<String> deleteAlbum(@PathVariable Integer albumId) {
    albumService.deleteAlbum(albumId);

    ApiResponse<String> apiResponse = new ApiResponse<>();
    apiResponse.setStatus("success");
    apiResponse.setMessage("Delete ALbum succesfully.");
    apiResponse.setData(null);

    return apiResponse;
  }

  @GetMapping
  ApiResponse<List<AlbumResponse>> getMusic(HttpServletRequest request) {
    @SuppressWarnings("unchecked")
    Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
    Integer id = Integer.parseInt(String.valueOf(user.get("id")));
    List<AlbumResponse> albums = albumService.getMusic(id);

    ApiResponse<List<AlbumResponse>> apiResponse = new ApiResponse<List<AlbumResponse>>();
    apiResponse.setStatus("success");
    apiResponse.setMessage("Get album successfilly");
    apiResponse.setData(albums);
    return apiResponse;
  }
}
