package com.TLU.SoundVerse.controller;

import java.util.Map;
// import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.TLU.SoundVerse.dto.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.AccessLevel;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminController {
    @GetMapping("check")
    public ApiResponse<Boolean> checkAdmin(HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        Map<String, Object> user = (Map<String, Object>) request.getAttribute("user");

        String role = (String) user.get("role");  

        ApiResponse<Boolean> apiResponse = new ApiResponse<Boolean>();

        apiResponse.setStatus("success");
        apiResponse.setMessage("Login successfilly");
        apiResponse.setData(role.equals("ADMIN"));
        return apiResponse;
    }
}