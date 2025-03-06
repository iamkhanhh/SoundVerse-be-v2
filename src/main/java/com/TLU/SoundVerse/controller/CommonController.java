package com.TLU.SoundVerse.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TLU.SoundVerse.dto.request.GetPresignedUrlForUploadDto;
import com.TLU.SoundVerse.dto.response.ApiResponse;
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
}