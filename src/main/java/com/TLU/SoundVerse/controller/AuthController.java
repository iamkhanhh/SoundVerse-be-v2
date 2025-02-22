package com.TLU.SoundVerse.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.TLU.SoundVerse.dto.request.LoginDto;
import com.TLU.SoundVerse.dto.request.VerifiDto;
import com.TLU.SoundVerse.dto.request.RegisterUserDto;
import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.dto.response.AuthResponse;
import com.TLU.SoundVerse.entity.User;
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
  @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authService.signup(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/verify")
        public ResponseEntity<?> verifyUser(@RequestBody VerifiDto verifyUserDto) {
            try {
                authService.verifyUser(verifyUserDto);
                return ResponseEntity.ok("Account verified successfully");
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
    }

    @PostMapping("/resend")
            public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
                try {
                    authService.resendVerificationCode(email);
                    return ResponseEntity.ok("Verification code sent");
                } catch (RuntimeException e) {
                    return ResponseEntity.badRequest().body(e.getMessage());
                }
            }
}
