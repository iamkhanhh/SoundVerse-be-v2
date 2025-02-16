package com.TLU.SoundVerse.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.TLU.SoundVerse.dto.request.LoginDto;
import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.dto.response.AuthResponse;
import com.TLU.SoundVerse.service.AuthService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {
  AuthService authService;

  @PostMapping("/log-in")
  ApiResponse<AuthResponse> authenticate(@RequestBody LoginDto request, HttpServletResponse response) {
    String token = authService.authenticate(request);

    Cookie jwtCookie = new Cookie("access_token", token);
    jwtCookie.setHttpOnly(true);
    jwtCookie.setSecure(true);
    jwtCookie.setPath("/");
    jwtCookie.setMaxAge(60 * 60 * 24 * 5);

    response.addCookie(jwtCookie);

    ApiResponse<AuthResponse> apiResponse = new ApiResponse<AuthResponse>();

    apiResponse.setStatus("success");
    apiResponse.setMessage("Login successfilly");
    return apiResponse;
  }

  // @PostMapping("/me")
  // ApiResponse<IntrospectResponse> introspect(@SuppressWarnings("rawtypes")
  // @RequestBody IntrospectRequest request)
  // throws JOSEException, ParseException {
  // var result = authService.introspect(request);

  // return ApiResponse.<IntrospectResponse>builder().data(result).build();
  // }
}
