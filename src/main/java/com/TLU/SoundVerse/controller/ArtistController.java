package com.TLU.SoundVerse.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.dto.response.ArtistResponse;
import com.TLU.SoundVerse.service.ArtistService;
import com.TLU.SoundVerse.service.CommonService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("artist")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ArtistController {
  CommonService commonService;
  ArtistService artistService;

  @GetMapping("/{artistId}")
  ApiResponse<ArtistResponse> getAlbumById(@PathVariable Integer artistId) {
    ArtistResponse artist = commonService.getArtistById(artistId);

    ApiResponse<ArtistResponse> apiResponse = new ApiResponse<ArtistResponse>();
    apiResponse.setStatus("success");
    apiResponse.setMessage("Get album successfilly");
    apiResponse.setData(artist);
    return apiResponse;
  }

  @GetMapping("/my-stats")
  public ApiResponse<Map<String, Integer>> getStatistics(HttpServletRequest request) {
    System.out.println("hellloo");
    Map<String, Integer> stats = artistService.getArtistStats(request);
    return new ApiResponse<>(200, "Get statistics successfully", "success", stats);
  }
}
