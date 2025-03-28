package com.TLU.SoundVerse.service;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.TLU.SoundVerse.dto.request.RegisterUserDto;
import com.TLU.SoundVerse.dto.request.UserUpdateDto;
import com.TLU.SoundVerse.dto.response.UserResponse;
import com.TLU.SoundVerse.entity.Artist;
import com.TLU.SoundVerse.entity.User;
import com.TLU.SoundVerse.enums.UserStatus;
import com.TLU.SoundVerse.mapper.UserMapper;
import com.TLU.SoundVerse.repository.ArtistRepository;
import com.TLU.SoundVerse.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    ArtistRepository artistRepository;
    UserMapper userMapper;
    ArtistService artistService;
    S3Service s3Service;

    public User create(RegisterUserDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email has been already used, please use another email!");
        }

        User user = userMapper.toUser(request);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userRepository.save(user);
    }

    public List<UserResponse> getUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::toUserResponse)
                .collect(Collectors.toList());
    }

    // public User updateUser(String userId, UpdateUserDto updateUserDto) {
    // User user = userRepository.findById(userId).orElseThrow(() -> new
    // RuntimeException("User Not Found"));

    // usermapper.updateUser(user, updateUserDto);

    // return usermapper.toUserResponseDto(userRepository.save(user));
    // }

    public void deleteUser(String userId) {
        Integer id = Integer.parseInt(userId);
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setStatus(UserStatus.DELETED);
            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public Map<String, String> getUsernameAndIdByArtistId(Integer artistId) {
        Integer userId = artistService.getArtistIdByUserId(artistId);
        return userRepository.findById(userId)
                .map(user -> {
                    Map<String, String> response = new HashMap<>();
                    response.put("id", String.valueOf(user.getId()));
                    response.put("username", user.getUsername());
                    return response;
                })
                .orElseThrow(() -> new RuntimeException("User not found!"));
    }

    public User getUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found!"));
    }

    public UserResponse getUserDetail(String userId) {
        Integer id = Integer.parseInt(userId);
        return toUserResponse(getUserById(id));
    }

    public Integer getArtistIdByUserId(Integer userId) {

        Artist artist = artistRepository.findByUserId(userId);

        return artist.getId();
    }

    public UserResponse getUserInfo(HttpServletRequest request) {
        Integer userId = getUserIdFromRequest(request);

        if (userId == null) {
            throw new RuntimeException("User ID not found in request");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        return toUserResponse(user);
    }

    private Integer getUserIdFromRequest(HttpServletRequest request) {
        Object userObj = request.getAttribute("user");

        if (userObj instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> user = (Map<String, Object>) userObj;

            Object idObj = user.get("id");
            if (idObj != null) {
                try {
                    return Integer.parseInt(idObj.toString());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }

    public User updateUser(Integer userId, UserUpdateDto updateDto) {
        if (userId == null) {
            throw new RuntimeException("User ID not found in request");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        if (updateDto.getUsername() != null)
            user.setUsername(updateDto.getUsername());
        if (updateDto.getGender() != null)
            user.setGender(updateDto.getGender());
        if (updateDto.getCountry() != null)
            user.setCountry(updateDto.getCountry());
        if (updateDto.getFullName() != null)
            user.setFullName(updateDto.getFullName());
        if (updateDto.getProfilePicImage() != null)
            user.setProfilePicImage(userId + "/thumbnails/" + updateDto.getProfilePicImage());
        if (updateDto.getDob() != null)
            user.setDob(updateDto.getDob());

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);
        if (updateDto.getCurrentPassword() != null && updateDto.getPassword() != null) {
            if (!passwordEncoder.matches(updateDto.getCurrentPassword(), user.getPassword())) {
                throw new RuntimeException("Current password is incorrect");
            }
            user.setPassword(passwordEncoder.encode(updateDto.getPassword()));
        }
        if (updateDto.getRole() != null) 
            user.setRole(updateDto.getRole());

  
        if (updateDto.getStatus() != null) 
            user.setStatus(updateDto.getStatus());

        return userRepository.save(user);
    }

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .gender(user.getGender())
                .country(user.getCountry())
                .status(user.getStatus())
                .role(user.getRole())
                .profilePicImage(user.getProfilePicImage() != null ? s3Service.getS3Url(user.getProfilePicImage())
                        : "/default_avatar_user.jpg")
                .fullName(user.getFullName())
                .dob(user.getDob())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
