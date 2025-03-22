package com.TLU.SoundVerse.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.TLU.SoundVerse.dto.request.ForgotPasswordDto;
import com.TLU.SoundVerse.dto.request.LoginDto;
import com.TLU.SoundVerse.dto.request.VerifiDto;
import com.TLU.SoundVerse.dto.request.RegisterUserDto;
import com.TLU.SoundVerse.dto.response.ApiResponse;
import com.TLU.SoundVerse.dto.response.AuthResponse;
import com.TLU.SoundVerse.dto.response.UserResponse;
import com.TLU.SoundVerse.service.AuthService;
import com.TLU.SoundVerse.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
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
    UserService userService;

    @PostMapping("/login")
    ApiResponse<AuthResponse> authenticate(@RequestBody LoginDto request, HttpServletResponse response) {
        String token = authService.authenticate(request);

        Cookie jwtCookie = new Cookie("access_token", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false); // Để chạy trên localhost, nếu production thì phải `true`
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60 * 24 * 5);
        jwtCookie.setAttribute("SameSite", "Strict");

        response.addCookie(jwtCookie);

        ApiResponse<AuthResponse> apiResponse = new ApiResponse<AuthResponse>();

        apiResponse.setStatus("success");
        apiResponse.setMessage("Login successfilly");
        return apiResponse;
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("access_token", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        jwtCookie.setAttribute("SameSite", "Strict");

        response.addCookie(jwtCookie);

        ApiResponse<Void> apiResponse = new ApiResponse<>();
        apiResponse.setStatus("success");
        apiResponse.setMessage("Logout successfully");
        return apiResponse;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getUserById(HttpServletRequest request) {
        UserResponse userResponse = userService.getUserInfo(request);
        return ResponseEntity.ok(userResponse);
    }


    @PostMapping("/signup")
    ApiResponse<AuthResponse> register(@RequestBody RegisterUserDto registerUserDto, HttpServletResponse response) {
        String token = authService.signup(registerUserDto);

        Cookie jwtCookie = new Cookie("access_token", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false); // Để chạy trên localhost, nếu production thì phải `true`
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(60 * 60 * 24 * 5);
        jwtCookie.setAttribute("SameSite", "Strict");

        response.addCookie(jwtCookie);

        ApiResponse<AuthResponse> apiResponse = new ApiResponse<AuthResponse>();

        apiResponse.setStatus("success");
        apiResponse.setMessage("Signup successfilly");
        return apiResponse;
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<?>> verifyUser(@RequestBody VerifiDto verifyUserDto) {
        try {
            authService.verifyUser(verifyUserDto);
            ApiResponse<?> response = ApiResponse.<Void>builder()
                .code(200)
                .status("success")
                .message("Account verified successfully")
                .build();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<?> errorResponse = ApiResponse.<Void>builder()
                .code(400)
                .status("failed")
                .message(e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<ApiResponse<?>> resendVerificationCode(@RequestParam String email) {
        try {
            authService.resendVerificationCode(email);
            ApiResponse<?> response = ApiResponse.<Void>builder()
                .code(200)
                .status("success")
                .message("Verification code sent")
                .build();
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            ApiResponse<?> errorResponse = ApiResponse.<Void>builder()
                .code(400)
                .status("failed")
                .message(e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<ApiResponse<?>> forgotPassword(@RequestBody ForgotPasswordDto input) {
        try {
            authService.forgotPassword(input);
            ApiResponse<?> successResponse = ApiResponse.<Void>builder()
                .code(200)
                .status("success")
                .message("Change Password Successfully")
                .build();
            return ResponseEntity.ok(successResponse);
        } catch (RuntimeException e) {
            ApiResponse<?> errorResponse = ApiResponse.<Void>builder()
                .code(400)
                .status("failed")
                .message(e.getMessage())
                .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

}
