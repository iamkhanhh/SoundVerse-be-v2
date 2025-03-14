package com.TLU.SoundVerse.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
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
import com.TLU.SoundVerse.service.AuthService;

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

    @PostMapping("/me")
    public ResponseEntity<?> getMyProfile(HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");
        
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        String id = String.valueOf(user.get("id"));  
        String email = (String) user.get("email");  
        String username = (String) user.get("username");  
        String role = (String) user.get("role");  
    
        System.out.println("ID: " + id);
        System.out.println("Email: " + email);
        System.out.println("Username: " + username);
        System.out.println("Role: " + role);

        return ResponseEntity.ok(user);
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
        apiResponse.setMessage("Login successfilly");
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
}
