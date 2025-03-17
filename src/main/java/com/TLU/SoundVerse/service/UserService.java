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
import com.TLU.SoundVerse.dto.response.AlbumResponse;
import com.TLU.SoundVerse.dto.response.ArtistResponse;
import com.TLU.SoundVerse.dto.response.MusicResponse;
import com.TLU.SoundVerse.dto.response.UserResponse;
import com.TLU.SoundVerse.entity.Artist;
import com.TLU.SoundVerse.entity.User;
import com.TLU.SoundVerse.enums.UserStatus;
import com.TLU.SoundVerse.mapper.UserMapper;
import com.TLU.SoundVerse.repository.ArtistRepository;
import com.TLU.SoundVerse.repository.UserRepository;

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

    public Map<String, String> getUsernameAndIdById(Integer userId) {
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

    public Integer getArtistIdByUserId(Integer userId) {

        Artist artist = artistRepository.findByUserId(userId);

        return artist.getId();
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
            .profilePicImage(user.getProfilePicImage())
            .fullName(user.getFullName())
            .dob(user.getDob())
            .createdAt(user.getCreatedAt())
            .build();
  } 
}
