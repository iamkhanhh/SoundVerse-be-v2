package com.TLU.SoundVerse.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.dto.response.ArtistResponse;
import com.TLU.SoundVerse.service.CommonService;
import com.TLU.SoundVerse.service.ArtistService;

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

  @GetMapping("/check-signed")
  ApiResponse<Boolean> checkSigned(HttpServletRequest request) {
    @SuppressWarnings("unchecked")
    Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
    Integer id = Integer.parseInt(String.valueOf(user.get("id")));
    Boolean isSigned = artistService.checkSigned(id);

    ApiResponse<Boolean> apiResponse = new ApiResponse<Boolean>();
    apiResponse.setStatus("success");
    apiResponse.setMessage("Get checkSigned successfilly");
    apiResponse.setData(isSigned);
    return apiResponse;
  }

  @PostMapping("/sign-contract")
  ApiResponse<Boolean> checkSigned(HttpServletRequest request) {
    @SuppressWarnings("unchecked")
    Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
    Integer id = Integer.parseInt(String.valueOf(user.get("id")));
    Boolean isSigned = artistService.checkSigned(id);

    ApiResponse<Boolean> apiResponse = new ApiResponse<Boolean>();
    apiResponse.setStatus("success");
    apiResponse.setMessage("Get checkSigned successfilly");
    apiResponse.setData(isSigned);
    return apiResponse;
  }
}
