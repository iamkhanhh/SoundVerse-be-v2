package com.TLU.SoundVerse.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.TLU.SoundVerse.dto.request.ForgotPasswordDto;
import com.TLU.SoundVerse.dto.request.LoginDto;
import com.TLU.SoundVerse.dto.request.RegisterUserDto;
import com.TLU.SoundVerse.dto.request.VerifiDto;
import com.TLU.SoundVerse.entity.User;
import com.TLU.SoundVerse.enums.UserRole;
import com.TLU.SoundVerse.enums.UserStatus;
import com.TLU.SoundVerse.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.AccessLevel;
import com.TLU.SoundVerse.service.EmailService;
import com.TLU.SoundVerse.service.ArtistService;

@SuppressWarnings("unused")
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserRepository userRepository;
    EmailService emailService;
    ArtistService artistService;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

    public String authenticate(LoginDto request) {
                    var user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User Not Found"));
    
                    boolean isAuthenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!isAuthenticated) {
            throw new RuntimeException("Password did not match");
        }

        if (user.getStatus() == UserStatus.DELETED) {
            throw new RuntimeException("Your account has been deleted! Please contact us to get more detail"); 
        } else if (user.getStatus() == UserStatus.DISABLED) {
            throw new RuntimeException("Your account has been disabled! Please contact us to get more detail"); 
        } else if (user.getStatus() == UserStatus.PENDING) {
            throw new RuntimeException("Your account has not been active! Please contact us to active your account"); 
        }

        return generateToken(user.getId(), user.getEmail(), user.getUsername(), user.getRole());
    }

    private String generateToken(Integer id, String email, String username, UserRole role) {
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
            JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                    .issuer("SoundVerse.com")
                    .issueTime(new Date())
                    .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()))
                    .claim("id", String.valueOf(id))
                    .claim("email", email)
                    .claim("username", username)
                    .claim("role", role)
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String signup(RegisterUserDto input) {
        if (userRepository.existsByEmail(input.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setUsername(input.getUsername());
        user.setEmail(input.getEmail());
        user.setDob(input.getDob());
        user.setCountry(input.getCountry());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusMinutes(15));
        user.setStatus(UserStatus.PENDING);
        user.setRole(input.getIsArtist() ? UserRole.ARTIST : UserRole.USER);
        user.setGender(input.getGender());
        user.setFullName(input.getFullName());

        sendVerificationEmail(user);
        userRepository.save(user);
        artistService.createArtist(user.getId());
        return generateToken(user.getId(), user.getEmail(), user.getUsername(), user.getRole());
    }

    public void verifyUser(VerifiDto input) {
        User user = userRepository.findByEmail(input.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getVerificationCodeExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification code has expired");
        }

        if (!user.getVerificationCode().equals(input.getVerificationCode())) {
            throw new RuntimeException("Invalid verification code");
        }

        user.setStatus(UserStatus.ACTIVE);
        user.setVerificationCode(null);
        user.setVerificationCodeExpiresAt(null);
        userRepository.save(user);
    }

    public void resendVerificationCode(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getStatus() == UserStatus.ACTIVE) {
            throw new RuntimeException("Account is already verified");
        }

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationCodeExpiresAt(LocalDateTime.now().plusHours(1));
        sendVerificationEmail(user);
        userRepository.save(user);
    }

    private void sendVerificationEmail(User user) {
        String subject = "Account Verification";
        String verificationCode = "VERIFICATION CODE " + user.getVerificationCode();
        String htmlMessage = "<html>"
                + "<body style=\"font-family: Arial, sans-serif;\">"
                + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        try {
            emailService.sendVerificationEmail(user.getEmail(), subject, htmlMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String generateVerificationCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000);
    }

    public void forgotPassword(ForgotPasswordDto input) {
        User user = userRepository.findByEmail(input.getEmail())
        .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(input.getNewPassword())); 
        userRepository.save(user);
    }

}
