package com.TLU.SoundVerse.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TLU.SoundVerse.dto.request.CreateAlbumDto;
import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.entity.Album;
import com.TLU.SoundVerse.service.AlbumService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("genre")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AlbumController {
  AlbumService albumService;

  @PostMapping
  ApiResponse<Album> createGenre(@RequestBody CreateAlbumDto createAlbumDto, HttpServletRequest request) {
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
}
